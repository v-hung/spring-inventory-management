package com.example.inventory_management.model;

import java.util.List;

import com.example.inventory_management.model.util.TimeStampCustom;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends TimeStampCustom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer id;

  @NotBlank
  public String name;

  public String code;

  public String barcode;

  @Column(columnDefinition = "TEXT")
  private String content;
  
  private String image;

  private Double cost;

  private Double price;

  @ManyToMany
  @JoinTable(
    name = "pivot_product_category",
    joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "collection_id", referencedColumnName = "id")
  )
  @JsonIgnoreProperties("products")
  private List<Category> categories;
}
