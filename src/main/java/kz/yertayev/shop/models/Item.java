package kz.yertayev.shop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stars;
    private String smallPicURL;
    private String largePicURL;
    private Date addedDate;
    private boolean inTopPage;
    @ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Categories> categories;
}
