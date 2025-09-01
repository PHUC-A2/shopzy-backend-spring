<!-- từ #6 đến #7 -->
    - Tạo các API login,logout,account,refresh(token)
    - Cấu hình Security

<!-- Cấu hình security với các dependency sau -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

<!--  Giai đoạn 1-->

    Thiết kế Giai đoạn 1
    1. User (Người dùng)
    id : Mã người dùng
    name : Tên
    fullName : Họ và tên
    email : Email đăng nhập
    password : Mật khẩu
    phoneNumber : Số điện thoại cá nhân (liên hệ tài khoản, không phải số nhận hàng đơn)
    status:  ACTIVE, // Hoạt động bình thường INACTIVE, // Ngưng hoạt động / bị vô hiệu hóa  PENDING_VERIFICATION, // Chờ xác minhemail/OTP BANNED, // Bị cấmDELETED // Đã xóa (hoặc lưu trữ)
    createdAt, createdBy, updatedAt, updatedBy
    2. Product (Sản phẩm)
    id : Mã sản phẩm
    name : Tên sản phẩm
    description : Mô tả sản phẩm
    price : Giá bán
    stock : Số lượng tồn kho
    status : Trạng thái hàng (IN_STOCK = còn hàng, OUT_OF_STOCK = hết hàng)
    condition : Tình trạng hàng (NEW = hàng mới, USED = hàng cũ/second-hand)
    imageUrl : Ảnh sản phẩm
    size : Kích thước (S, M, L, XL)
    color : Màu sắc
    createdAt, createdBy, updatedAt, updatedBy
    3. Cart (Giỏ hàng)
    id : Mã giỏ hàng
    userId : FK → User (người sở hữu giỏ hàng)
    createdAt, createdBy, updatedAt, updatedBy
    4. CartItem (Sản phẩm trong giỏ hàng)
    id : Mã item
    cartId : FK → Cart
    productId : FK → Product
    quantity : Số lượng
    createdAt, createdBy, updatedAt, updatedBy
    5. Order (Đơn hàng)
    id : Mã đơn hàng
    userId : FK → User (người đặt)
    status : Trạng thái đơn hàng (PENDING, SHIPPING, COMPLETED, CANCELLED)
    paymentMethod : Phương thức thanh toán (COD, VNPAY…)
    paymentStatus : Trạng thái thanh toán (UNPAID, PAID)
    total : Tổng tiền
    shippingAddress : Địa chỉ giao hàng
    shippingPhone : Số điện thoại nhận hàng
    createdAt, createdBy, updatedAt, updatedBy
    6. OrderItem (Chi tiết đơn hàng)
    id : Mã chi tiết đơn
    orderId : FK → Order
    productId : FK → Product
    quantity : Số lượng đặt
    unitPrice : Giá tại thời điểm đặt
    createdAt, createdBy, updatedAt, updatedBy
    👉 Với thiết kế này:
    CRUD cho User → Product → Cart → CartItem → Order → OrderItem theo thứ tự.



<!--  -->