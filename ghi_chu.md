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

<!-- Giai đoạn 2 -->
    Phân tích mối quan hệ 8 entity
    1. User (Người dùng)
    1 User → N Cart (1 người có thể có nhiều giỏ hàng, nhưng thường hệ thống thực tế sẽ cho 1 giỏ hàng active thôi).
    1 User → N Order (1 người có thể đặt nhiều đơn hàng).
    1 User → 1 Role (quan hệ phân quyền).
    User là gốc để bắt đầu, nên bạn làm CRUD User trước.
    2. Product (Sản phẩm)
    1 Product → N CartItem (sản phẩm có thể nằm trong nhiều giỏ hàng).
    1 Product → N OrderItem (sản phẩm có thể được bán trong nhiều đơn hàng).
    CRUD Product độc lập, không phụ thuộc gì trước đó, nên làm sau User.
    3. Cart (Giỏ hàng)
    1 Cart → N CartItem (giỏ hàng chứa nhiều sản phẩm).
    1 Cart → 1 User (giỏ hàng thuộc về 1 người dùng).
    Nên làm sau User và Product (vì Cart cần userId, CartItem cần productId).
    4. CartItem (Sản phẩm trong giỏ hàng)
    1 CartItem → 1 Cart (thuộc về giỏ hàng nào).
    1 CartItem → 1 Product (tham chiếu đến sản phẩm nào).
    Đây là bảng trung gian (Cart – Product), nên CRUD CartItem phải làm sau khi có Cart + Product.
    5. Order (Đơn hàng)
    1 Order → N OrderItem.
    1 Order → 1 User (đơn hàng do user đặt).
    Order phụ thuộc User (ai đặt) và Cart (lấy dữ liệu từ giỏ hàng để tạo đơn).
    CRUD Order nên làm sau khi xong Cart/CartItem.
    6. OrderItem (Chi tiết đơn hàng)
    1 OrderItem → 1 Order.
    1 OrderItem → 1 Product.
    Đây cũng là bảng trung gian (Order – Product), nên CRUD OrderItem phải làm sau Order + Product.
    7. Role (Vai trò)
    1 Role → N User.
    Vai trò chỉ để phân loại user, không ảnh hưởng đến Order/Product nên để cuối.
    8. Permission (Quyền hạn)
    N Role ↔ N Permission (nhiều-nhiều).
    Cũng chỉ để phân quyền, làm sau khi hệ thống chính đã chạy được.
    📌 Thứ tự CRUD hợp lý + lý do
    User → gốc cho các entity khác.
    Product → độc lập, không phụ thuộc.
    Cart → cần userId.
    CartItem → cần cartId + productId.
    Order → cần userId (người đặt) + tham chiếu từ Cart.
    OrderItem → cần orderId + productId.
    Role → gán quyền cho User.
    Permission → gán quyền cho Role.
    👉 Tóm gọn quan hệ chính:
    User 1–N Cart
    User 1–N Order
    User N–1 Role
    Role N–N Permission
    Cart 1–N CartItem – N–1 Product
    Order 1–N OrderItem – N–1 Product



    ======================================================
    Mỗi khi có quan hệ giữa các entity thì sẽ phát sinh khóa ngoại (foreign key), nhưng không phải quan hệ nào cũng cần thêm thuộc tính “riêng” ngoài FK.
    Mình phân tích từng mối quan hệ bạn liệt kê:
    1. User 1–N Cart
    Trong bảng Cart bạn cần thêm userId (FK → User.id).
    Không cần thêm attribute khác nếu không có thông tin đặc biệt về quan hệ này (ví dụ “Cart này đang active hay archived”).
    2. User 1–N Order
    Trong bảng Order bạn cần userId (FK → User.id).
    Không cần thêm attribute riêng.
    3. User N–1 Role
    Trong bảng User bạn đã có roleId (FK → Role.id).
    Vậy là đủ, không cần thêm gì nữa.
    4. Role N–N Permission
    Đây là quan hệ nhiều–nhiều nên phải có bảng trung gian.
    Ví dụ: Role_Permission (roleId, permissionId).
    Trong bảng trung gian này chỉ cần 2 FK, trừ khi bạn muốn thêm attribute đặc biệt (ví dụ “ngày gán quyền”).
    5. Cart 1–N CartItem – N–1 Product
    Bảng CartItem chính là bảng trung gian giữa Cart và Product.
    Các cột cần:
    cartId (FK → Cart.id)
    productId (FK → Product.id)
    quantity (số lượng sản phẩm trong giỏ) → đây chính là attribute bổ sung của quan hệ (không thể gắn trong Product hay Cart).
    6. Order 1–N OrderItem – N–1 Product
    Bảng OrderItem là bảng trung gian giữa Order và Product.
    Các cột cần:
    orderId (FK → Order.id)
    productId (FK → Product.id)
    quantity
    unitPrice (giá tại thời điểm đặt, để không bị thay đổi khi giá Product thay đổi) → đây cũng là attribute bổ sung của quan hệ.
    ✅ Kết luận:
    Quan hệ 1–N → chỉ cần thêm FK là đủ.
    Quan hệ N–N → bắt buộc có bảng trung gian, có thể có thêm attribute nếu cần.
    Với CartItem và OrderItem thì attribute bổ sung (quantity, unitPrice) là cần thiết.
    Với User–Role, User–Cart, User–Order thì chỉ cần FK, không cần thêm attribute khác.

    =============================================
    1. User – Cart
    Quan hệ: 1 User → N Cart
    Mapping:
    User → @OneToMany(mappedBy="user")
    Cart → @ManyToOne @JoinColumn(name="user_id")
    2. User – Order
    Quan hệ: 1 User → N Order
    Mapping:
    User → @OneToMany(mappedBy="user")
    Order → @ManyToOne @JoinColumn(name="user_id")
    3. User – Role
    Quan hệ: N User → 1 Role
    Mapping:
    Role → @OneToMany(mappedBy="role")
    User → @ManyToOne @JoinColumn(name="role_id")
    4. Role – Permission
    Quan hệ: N Role ↔ N Permission (nhiều–nhiều)
    Mapping:
    Tạo bảng trung gian role_permission (role_id, permission_id).
    Trong JPA:
    @ManyToMany @JoinTable( name = "role_permission", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id") ) private Set<Permission> permissions; 
    5. Cart – CartItem – Product
    Quan hệ:
    1 Cart → N CartItem
    1 Product → N CartItem
    CartItem là bảng trung gian, có thêm attribute quantity.
    Mapping:
    Cart → @OneToMany(mappedBy="cart")
    CartItem → @ManyToOne @JoinColumn(name="cart_id")
    Product → @OneToMany(mappedBy="product")
    CartItem → @ManyToOne @JoinColumn(name="product_id")
    6. Order – OrderItem – Product
    Quan hệ:
    1 Order → N OrderItem
    1 Product → N OrderItem
    OrderItem là bảng trung gian, có thêm attribute quantity, unitPrice.
    Mapping:
    Order → @OneToMany(mappedBy="order")
    OrderItem → @ManyToOne @JoinColumn(name="order_id")
    Product → @OneToMany(mappedBy="product")
    OrderItem → @ManyToOne @JoinColumn(name="product_id")
    ✅ Tóm tắt các quan hệ chính
    User 1–N Cart (FK: cart.user_id)
    User 1–N Order (FK: order.user_id)
    User N–1 Role (FK: user.role_id)
    Role N–N Permission (bảng trung gian role_permission)
    Cart 1–N CartItem – N–1 Product (cart_id, product_id, quantity)
    Order 1–N OrderItem – N–1 Product (order_id, product_id, quantity, unitPrice


<!--  -->