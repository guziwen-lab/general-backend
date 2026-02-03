package com.supermap.shiro.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author gzw
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TokenUsernamePasswordDTO {

    private String token;

    private String username;

    private String password;

}
