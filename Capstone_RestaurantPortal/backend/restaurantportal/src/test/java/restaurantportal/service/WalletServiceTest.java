package restaurantportal.service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import restaurantportal.dto.*;
import restaurantportal.entity.User;
import restaurantportal.repository.UserRepository;
import restaurantportal.security.SecurityUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    @Mock private UserRepository userRepository;

    @InjectMocks private WalletService walletService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    private User user(double balance) {
        User u = new User();
        u.setId(1L);
        u.setEmail("test@mail.com");
        u.setWalletBalance(balance);
        return u;
    }

    private AddMoneyRequest request(double amount) {
        AddMoneyRequest r = new AddMoneyRequest();
        r.setAmount(amount);
        return r;
    }

    @Test
    void addMoney_success() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@mail.com");

            User u = user(100.0);

            when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(u));
            when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            WalletResponse res = walletService.addMoney(request(50.0));

            assertEquals(150.0, res.getBalance());
        }
    }

    @Test
    void addMoney_userNotFound() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            when(userRepository.findByEmail("x")).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> walletService.addMoney(request(50.0)));
        }
    }

    @Test
    void addMoney_zeroAmount() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@mail.com");

            User u = user(100.0);

            when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(u));
            when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            WalletResponse res = walletService.addMoney(request(0.0));

            assertEquals(100.0, res.getBalance());
        }
    }

    @Test
    void addMoney_negativeAmount() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@mail.com");

            User u = user(100.0);

            when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(u));
            when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            WalletResponse res = walletService.addMoney(request(-20.0));

            assertEquals(80.0, res.getBalance());
        }
    }

    @Test
    void getBalance_success() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("test@mail.com");

            User u = user(200.0);

            when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(u));

            WalletResponse res = walletService.getBalance();

            assertEquals(200.0, res.getBalance());
        }
    }

    @Test
    void getBalance_userNotFound() {
        try (MockedStatic<SecurityUtil> s = mockStatic(SecurityUtil.class)) {

            s.when(SecurityUtil::getCurrentUserEmail).thenReturn("x");

            when(userRepository.findByEmail("x")).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> walletService.getBalance());
        }
    }
}