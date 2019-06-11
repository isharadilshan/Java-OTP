package otp.java;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class OTPJAVA {
    
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static String APP_EMAIL = "dilshanishara73@gmail.com"; 
    private static String APP_PASSWORD = "deuhsbrqklvcortf"; 
    public static final int OTP_LENGTH = 10;
    public static final String CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String SIMPLE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String NUMBERS = "0123456789";
    
    private static final ArrayList<String> otpList = new ArrayList<>();
    


    public static void main(String[] args) {
        
        selectOption();
    }
    
    private static void selectOption(){
    
        System.out.println("\nWelcome to OTP Generator!!!");
        System.out.println("Please select a Option below");
        System.out.println("\n1. Generate an OTP");
        System.out.println("2. Verify an OTP");
        System.out.println("\nEnter option Number");
        
        Scanner input = new Scanner(System.in);
        int option=0;
        try{
            option = input.nextInt();
        }catch(Exception e){
            System.out.println("Please input numerical value for Option");
        }
        
        switch(option){
            case 1:
                genOTP(OTP_LENGTH);
                break;
            case 2:
                verifyOTP();
                break;
            default:
                System.out.println("Please enter the correct option number here!!!");
               
        }
        
    }
    
    
    private static void genOTP(int n){
    
        System.out.print("\nEnter your email here--->");
        Scanner input = new Scanner(System.in);
        String userEmail = input.next();
        if(!emailValidater(userEmail)){
            System.out.println("\nPlease input a valid email here..");
            genOTP(OTP_LENGTH);
        }
        
        String values = CAPITAL_LETTERS+SIMPLE_LETTERS+NUMBERS;
        
        Random random_method = new Random();
        
        String otpPassword = "";
        
        for(int i=0;i<OTP_LENGTH;i++){
            otpPassword = otpPassword+values.charAt(random_method.nextInt(values.length()));
        }
       
        otpList.add(otpPassword);
        sendEmail(userEmail,otpPassword);
    }
    
    public static void sendEmail(String email, String otp){
        Properties props= new Properties();
        
        props.put("mail.smtp.host", "smtp.gmail.com"); // host
        props.put("mail.smtp.port", "587"); // port
        props.put("mail.smtp.auth", "true"); // authentication method
        props.put("mail.smtp.starttls.enable","true"); //tls enabled or not

        System.out.println("Sending Mail to "+email+"...");
        System.out.println("Please Wait.....");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(APP_EMAIL, APP_PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("dilshanishara73@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject("Password Reset Request");
            message.setText("Dear User, \n Your new Password is "+otp);
            Transport.send(message);
            System.out.println("Message send Successfully");
            System.out.println("Check your inbox..");
            System.out.println(otp);
            System.out.println("\nIf you willing to check your OTP please follow the commands again");
            selectOption();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        
        
    }
    
    public static void verifyOTP(){
        System.out.println("\nEnter your OTP here--->");
        Scanner eOTP = new Scanner(System.in);
        String otp = eOTP.next();
        for(String item: otpList){
            if(otp == item){
                System.out.println("\nOTP is verified");
                System.out.println("\nIf you willing to check your OTP please follow the commands again");
                selectOption();
                return;
            }
        }
        System.out.println("OTP verification failed, Try again");
        selectOption();
        
    }

    public static boolean emailValidater(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
    
}

