package tacos.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.Taco;
import tacos.User;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

// tag::classShell[]
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

//end::classShell[]

    //tag::bothRepoProperties[]
//tag::ingredientRepoProperty[]
    private final IngredientRepository ingredientRepo;

    //end::ingredientRepoProperty[]
    private final TacoRepository designRepo;

    private final UserRepository userRepo;

//end::bothRepoProperties[]

  /*
// tag::ingredientRepoOnlyCtor[]
  @Autowired
  public DesignTacoController(IngredientRepository ingredientRepo) {
    this.ingredientRepo = ingredientRepo;
  }
// end::ingredientRepoOnlyCtor[]
   */

    //tag::bothRepoCtor[]
//    @Autowired
    public DesignTacoController(
            IngredientRepository ingredientRepo,
            TacoRepository designRepo,
            UserRepository userRepo) {
        this.ingredientRepo = ingredientRepo;
        this.designRepo = designRepo;
        this.userRepo = userRepo;
    }

    //end::bothRepoCtor[]

    // tag::modelAttributes[]
    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "design")
    public Taco taco() {
        return new Taco();
    }

    // end::modelAttributes[]
    // tag::showDesignForm[]

    @GetMapping
    public String showDesignForm(Model model, Principal principal) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(i -> ingredients.add(i));

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }

        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);

        return "design";
    }
//end::showDesignForm[]

    //tag::processDesign[]
    @PostMapping
    public String processDesign(
            @Valid Taco design, Errors errors,
            @ModelAttribute Order order) {

        if (errors.hasErrors()) {
            return "design";
        }

        Taco saved = designRepo.save(design);
        order.addDesign(saved);

        return "redirect:/orders/current";
    }
    //end::processDesign[]

    private List<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

  /*
//tag::classShell[]

  ...

//end::classShell[]
   */
//tag::classShell[]

}
//end::classShell[]
