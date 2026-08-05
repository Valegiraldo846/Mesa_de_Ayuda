package entities;

import java.time.LocalDateTime;

public class RefreshToken {
    private long id;
    private String token;
    private Usuario usuario;
    private LocalDateTime expiraEn;
    private boolean revocado;


}
