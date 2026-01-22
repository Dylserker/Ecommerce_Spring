package com.judy.ecommerce.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class PublicController {

    @GetMapping
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/products")
    public String showPublicProducts(Model model) {
        // TODO: Charger les produits depuis la base de données
        List<Map<String, Object>> products = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Map<String, Object> product = new HashMap<>();
            product.put("id", (long) i);
            product.put("name", "Produit " + i);
            product.put("category", i % 2 == 0 ? "Ordinateurs" : "Smartphones");
            product.put("price", 149.99 + i * 50);
            product.put("description", "Description du produit " + i);
            product.put("imageUrl", "/img/sample-product.jpg");
            products.add(product);
        }
        model.addAttribute("products", products);
        return "public/product_list";
    }

    @GetMapping("/product/{id}")
    public String showPublicProductDetail(@PathVariable Long id, Model model) {
        // TODO: Charger le produit depuis la base de données
        Map<String, Object> product = new HashMap<>();
        product.put("id", id);
        product.put("name", "Produit " + id);
        product.put("category", id % 2 == 0 ? "Ordinateurs" : "Smartphones");
        product.put("price", 199.0);
        product.put("originalPrice", 249.0);
        product.put("discount", 20);
        product.put("stock", 12);
        product.put("rating", 4.5);
        product.put("reviews", 128);
        product.put("description", "Description du produit " + id);
        product.put("brand", "Marque X");
        product.put("color", "Noir");
        product.put("size", "M");
        product.put("material", "Composite");
        product.put("weight", 1.2);
        product.put("sku", "SKU-" + id);
        product.put("imageUrl", "/img/sample-product.jpg");

        List<Map<String, Object>> related = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> rel = new HashMap<>();
            rel.put("id", id + i);
            rel.put("name", "Produit lié " + (id + i));
            rel.put("price", 149.0 + i * 10);
            rel.put("imageUrl", "/img/sample-product.jpg");
            related.add(rel);
        }

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", related);
        model.addAttribute("productId", id);
        return "public/product";
    }

    @GetMapping("/cart")
    public String showPublicCart(Model model) {
        // Initialiser les variables pour le panier public
        List<Object> cartItems = new ArrayList<>();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartEmpty", cartItems.isEmpty());
        model.addAttribute("subtotal", 0.0);
        model.addAttribute("tax", 0.0);
        model.addAttribute("total", 0.0);
        model.addAttribute("isPublic", true);
        return "public/panier";
    }
}
