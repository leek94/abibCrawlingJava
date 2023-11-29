package com.example.abibCrawlingJava.util;

import com.example.abibCrawlingJava.entiey.Product;
import com.example.abibCrawlingJava.repository.CcProductRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class Common {

    @Value("${spring.datasource.url}")
    private  String url;

    @Value("${spring.datasource.username}")
    private  String name;

    @Value("${spring.datasource.password}")
    private  String password;

    String siteType = "OL";


    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; // 드라이버 ID
    public static final String WEB_DRIVER_PATH = "C:\\Program Files\\selenium\\chromedriver.exe"; // 드라이버 경로

    private static final String IMAGE_DIRECTORY = "/uploadc/contents/image/";// 이미지 경로


    public double calculateDiscountPercent(String bePrice, String price) {
        if(bePrice.equals("0")){
            return 0.0;

        }

        int bePriceInt = Integer.parseInt(bePrice);
        int priceInt = Integer.parseInt(price);
        return ((bePriceInt - priceInt) / (double) bePriceInt * 100);
    }

    public String nullCheck(Element element) {

        return Optional.ofNullable(element)
                .map(Element::text)
                .orElse("");
    }

    public String nullCheckPrice(Element element) {

        return Optional.ofNullable(element)
                .map(Element::text) // text 메서드 실행
                .filter(text -> !text.equals("SOLD OUT")) // SOLD OUT이 아니면 다음으로 넘어감
                .map(text -> text.replaceAll("[^0-9]", "")) //정규 표현식으로 숫자 이외에 값 삭제
                .map(text -> {
                    try{
                        return String.valueOf(Integer.parseInt(text)); // 정수로 변경
                    } catch( NumberFormatException e){
                        return "0";
                    }
                })
                .orElse("0");
    }

    public String downloadImage(Product product) {
        String fileDirectory = IMAGE_DIRECTORY + product.getSiteType() + "/";
        //"/uploadc/contents/image/OL/"
//        String fileDirectory = "C:\\Users\\Focus\\image\\OL\\";
        String filePath = fileDirectory + product.getProdCode() + ".png";
        //"/uploadc/contents/image/OL/A000000166675.png"

        try{
            URL url = new URL("http:"+ product.getImg2());
            InputStream inputStream = url.openStream();

            Path path = Paths.get(fileDirectory);
            Files.createDirectories(path);

            if (!Files.exists(Paths.get(filePath))) {
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                fileOutputStream.close();
            }
            inputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return filePath;
    }

    public void sendMail(String message, String siteType) {
        try{
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            String title = siteType + " " + formattedDateTime + "오류 발생";
            String content = message;

            // SMTP 서버 설정
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.naver.com");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "587");

            // 계정 정보 설정
            String username = "solutionfocus@naver.com";
            String password = "!solfocus0515";

            // 메일 수신자 설정
            String receiver = "jh@solutionfocus.co.kr";

            //메일 세션 생성
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            //메시지 생성
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress((receiver)));
            msg.setSubject(title);
            msg.setText(content);

            //메일 전송
            Transport.send(msg);

        } catch (MessagingException e){
            e.printStackTrace();
        }
    }
    public void retryChecker(){
        int maxRetries = 3;
        int retryCount = 0;
        String url = "https://www.oliveyoung.co.kr/store/main/main.do?oy=0";
    }

    //    public static WebDriver startCrawling(String siteType , String url, String name, String password){
    public WebDriver startCrawling(String siteType) {

        WebDriverManager.chromedriver().setup(); // 드라이버 최신 버전 자동 업데이트

        // Chrome WebDriver 설정
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH); //서버에서는 위치를 환경변수나 경로를 서버에 맞게 지정해줘야함


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-allow-origins=*"); // 403 에러 해결을 위해 필요

        WebDriver driver = new ChromeDriver(options);


        // JDBC로 데이터베이스 연결
        try {
            Connection connection = DriverManager.getConnection(url, name, password); //로컬 데이터베이스
//            Connection connection = dataSource.getConnection(); // TODO : 현재 @Component로 받아와서 사용하는 방식으로 구현중 그런데 이렇게 하면 간단하게 테스트가 안됨 스프링 돌때매 @Values에 값이 들어옴 --> 테스트 방법 필요

            String findBatchYn = "SELECT batch_yn FROM cc_site WHERE site_type = ?";
            PreparedStatement statement = connection.prepareStatement(findBatchYn);
            statement.setString(1, siteType); // SQL injection 공격 방지하려고 동적으로 값 설정

            ResultSet resultSet = statement.executeQuery(); // 쿼리문 실행

            if (resultSet.next()) {
                String batchYn = resultSet.getString("batch_yn");

                if ("Y".equals(batchYn)) {
                    return driver;
                } else {
                    resultSet.close();
                    statement.close();
                    connection.close();
                    driver.quit();
                    System.exit(0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void endCrawling(WebDriver driver, String siteType) {
        log.info(siteType + "크롤링 종료");
        System.exit(0);
    }
}
