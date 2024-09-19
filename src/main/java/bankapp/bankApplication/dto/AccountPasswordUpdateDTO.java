package bankapp.bankApplication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountPasswordUpdateDTO {
    @NotBlank
    String oldPassword;
    @NotBlank
    String newPassword;
}
