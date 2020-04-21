package dev.solar.parsingreqdata;

import org.springframework.data.annotation.Id;

public class SideDish {
    @Id
    private Long id;
    private String detailHash;
    private Long mainImage;
    private Long topImage;
    private String alt;
    private String title;
    private String description;
    private Integer normal_price;
    private Integer sale_price;
    private Integer point;
    private String deliveryInfo;

}
