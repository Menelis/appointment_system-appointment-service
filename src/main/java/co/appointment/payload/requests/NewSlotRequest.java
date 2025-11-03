package co.appointment.payload.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewSlotRequest {

    @NotEmpty(message = "Slot Start cannot not be empty")
    @NotBlank(message = "Slot Start cannot be blank")
    @NotNull(message = "Slot Start cannot be null")
    @Size(message = "Only 20 characters allowed for Slot Start", max = 20)
    private String slotStart;

    @NotEmpty(message = "Slot End cannot not be empty")
    @NotBlank(message = "Slot End cannot be blank")
    @NotNull(message = "Slot End cannot be null")
    @Size(message = "Only 20 characters allowed for Slot End", max = 20)
    private String slotEnd;
}
