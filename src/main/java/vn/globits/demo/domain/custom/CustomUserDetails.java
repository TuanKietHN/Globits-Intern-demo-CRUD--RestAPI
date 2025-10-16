package vn.globits.demo.domain.custom;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.globits.demo.domain.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 🔹 Đổi lại cho rõ ràng: mỗi Role có field "role" (ví dụ: ADMIN, USER)
        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole()))
                .collect(Collectors.toSet());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // ✅ CHỖ ĐÃ SỬA
    // Vì entity User không có username riêng — bạn có thể chọn cách hiển thị nào hợp lý:
    // 1️⃣ Nếu bạn muốn dùng email làm username (thường dùng để đăng nhập)
    // 2️⃣ Nếu bạn muốn hiển thị tên người thật, lấy từ Person
    @Override
    public String getUsername() {
        // Nếu muốn hiển thị tên cá nhân thay vì email:
        if (user.getPerson() != null && user.getPerson().getFullName() != null) {
            return user.getPerson().getFullName();
        }
        // fallback: nếu Person chưa gán thì dùng email làm username
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive(); // 🔹 sử dụng đúng flag từ entity
    }

    public User getUser() {
        return user;
    }
}
