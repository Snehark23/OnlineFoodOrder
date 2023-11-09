package com.project.food.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import com.project.food.entity.Cart;
import com.project.food.entity.Category;

import com.project.food.entity.Product;
import com.project.food.entity.User;
import com.project.food.repo.CartRepo;

import com.project.food.repo.ProductRepo;
import com.project.food.repo.UserRepo;
import com.project.food.service.CartService;
import com.project.food.service.ProductService;

@Controller
@RequestMapping("/user")
public class CartController {
	@Autowired
	private CartService cartService;
	@Autowired
	private CartRepo cartRepo;
	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ProductService productService;



	@GetMapping("/cart")
	public String showShoppingCart(Model model, Principal principal) {
		String username = principal.getName(); // Get the username of the currently logged-in user
		User currentUser = userRepo.findByEmail(username);

		List<Cart> cartItems = cartService.listCartItems(currentUser);

		// Calculate the total amount
		double totalAmount = 0;
		for (Cart cartItem : cartItems) {
			totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();
		}

		model.addAttribute("cartItems", cartItems);
		// model.addAttribute("pageTitle", "Shopping Cart");
		model.addAttribute("totalAmount", totalAmount);
		return "cart";
	}

	@PostMapping("/cart/{productId}")
	public String addToCart(@PathVariable int productId, Principal principal,Model model) {
		// Get the currently logged-in user
		String username = principal.getName();
		
		

		// Retrieve the book with the given id (you'll need to implement this method)
		Product selectedProduct = productService.getProductsById(productId);

		// Add the book to the user's cart (you'll need to implement this method)
		cartService.addToCart(username, selectedProduct);

		model.addAttribute("addToCartSuccessMessage", selectedProduct.getName() + " has been added to your cart.");
		// Redirect the user to the "Available Books" page or another appropriate page
		return "redirect:/user/products";
	}

	@PostMapping("/user/deleteproduct/{productId}")
	public String deleteProduct(@RequestParam("productId") Integer productId) {
		cartService.deleteById(productId);
		return "redirect:/user/cart"; // Redirect to the product list page or another appropriate page
	}

}

//    @PostMapping("/user/deleteproduct/{productId}")
//    public String deleteProduct(@RequestParam("productId") Integer productId) {
//        cartService.deleteById(productId);
//        return "redirect:/user/cart"; // Redirect to the product list page or another appropriate page
//    }

	

	 
	
	
