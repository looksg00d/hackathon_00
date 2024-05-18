package com.smartbudget.DTO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryDTO {
    private long id;
    private String categoryName;
    private long customerId;

}
