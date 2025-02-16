# Online Marketplace Backend

Dự án này là backend của hệ thống Online Marketplace, được xây dựng bằng Java Spring Boot. Dưới đây là hướng dẫn để chạy và phát triển dự án.

## Cài đặt môi trường

Trước khi chạy dự án, bạn cần cấu hình môi trường và các biến môi trường. Hãy làm theo các bước sau:

### 1. Clone repository

Đầu tiên, clone repository về máy của bạn.

```bash
git clone https://github.com/username/online-marketplace-backend.git
cd online-marketplace-backend
```

### 2. Cài đặt biến môi trường
```bash
cp .env.template .env
```
Dự án này là backend của hệ thống Online Marketplace, được xây dựng bằng Java Spring Boot. Nó cung cấp các API cho phép người dùng đăng ký, đăng nhập, quản lý tài khoản, và thực hiện các giao dịch trên nền tảng.

## Mục lục

- [Cài đặt môi trường](#cài-đặt-môi-trường)
- [Cấu hình](#cấu-hình)
- [Các tính năng](#các-tính-năng)
    - [Xác thực (Authentication)](#xác-thực-authentication)
        - [Đăng ký (Register)](#đăng-ký-register)
        - [Đăng nhập (Login)](#đăng-nhập-login)
        - [Quên mật khẩu (Forgot Password)](#quên-mật-khẩu-forgot-password)
        - [Đăng xuất (Logout)](#đăng-xuất-logout)
    - [Quản lý người dùng (User Management)](#quản-lý-người-dùng-user-management)
    - [Quản lý sản phẩm (Product Management)](#quản-lý-sản-phẩm-product-management)
    - [Quản lý đơn hàng (Order Management)](#quản-lý-đơn-hàng-order-management)
- [API](#api)
- [Hướng dẫn phát triển](#hướng-dẫn-phát-triển)
- [Đóng góp](#đóng-góp)
- [Giấy phép](#giấy-phép)
