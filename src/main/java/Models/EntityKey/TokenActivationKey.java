package Models.EntityKey;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class TokenActivationKey implements Serializable {
    private String token;

    private UUID user_id;
}
