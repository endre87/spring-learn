// tag::all[]
// tag::allButValidation[]
package tacos;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

// end::allButValidation[]
// tag::allButValidation[]

@Data
public class Taco {

    private Long id;

    private Date createdAt;

    // end::allButValidation[]
    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    // tag::allButValidation[]
    private String name;
    // end::allButValidation[]
    @NotNull
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    // tag::allButValidation[]
    private List<String> ingredients;

}
//end::allButValidation[]
//tag::end[]