package dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CaptchaDto {
    private String captchaId;
    private String captchaImg;
}
