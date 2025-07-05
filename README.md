# 🛒 Java E-Commerce System

A simple and extensible **Java-based e-commerce system** that demonstrates core OOP concepts including:

- Inheritance & Abstraction
- Interfaces (`Shippable`, `Expirable`)
- Composition (`Cart`, `Customer`, `CheckoutService`)
- Edge case handling (stock limits, expired products, shipping calculations)

---

## ✨ Features

- **Product Catalog** with different types: _Cheese, Biscuits, TV, Mobile Scratch Card_.
- Some products:
  - **Expire** (e.g., Cheese, Biscuits)
  - **Require shipping** (e.g., Cheese, TV)
- **Cart System** with quantity checks and stock validation.
- **Checkout Flow**:
  - Calculates subtotal and shipping fees
  - Prevents checkout if:
    - The cart is empty
    - A product is expired
    - A product is out of stock
    - Customer has insufficient balance
- **Shipping Service** for shippable products

---

## 📦 Classes Overview

### 📁 `Product` (abstract class)
- `name`, `price`, `quantity`
- Subclasses: `Cheese`, `Biscuits`, `TV`, `MobileScratchCard`, `Mobile`

### 🧾 Interfaces
- **`Shippable`** → requires `getName()` and `getWeight()`
- **`Expirable`** → requires `isExpired()`

### 🧍‍♂️ `Customer`
- Tracks customer `name` and `balance`

### 🛒 `Cart`
- Adds items to the cart
- Validates quantity
- Used in checkout

### 📦 `ShippingService`
- Prints shipping notice with total package weight

### 💳 `CheckoutService`
- Handles the checkout flow
- Applies all validation rules and prints receipt

