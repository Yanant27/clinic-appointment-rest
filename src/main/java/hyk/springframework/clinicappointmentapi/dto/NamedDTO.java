package hyk.springframework.clinicappointmentapi.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * @author Htoo Yanant Khin
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class NamedDTO extends BaseDTO{
    @NotEmpty
    private String name;

    public NamedDTO(Long id, String name) {
        super(id);
        this.name = name;
    }
}
