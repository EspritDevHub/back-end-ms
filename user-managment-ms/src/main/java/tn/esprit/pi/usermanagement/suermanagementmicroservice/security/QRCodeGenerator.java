package tn.esprit.pi.usermanagement.suermanagementmicroservice.security;

import java.io.ByteArrayOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Entities.User;

@Component
public class QRCodeGenerator {
    public String generateQRCodeForUser(User user) {
        try {
            String issuer = "EspritDevHub";
            String email = user.getEmail();
            String secretKey = user.getSecretKey();

            // Google Authenticator-compatible URI
            String qrCodeUrl = "otpauth://totp/" + issuer + ":" + email + "?secret=" + secretKey + "&issuer=" + issuer;

            int width = 250;
            int height = 250;
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeUrl, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            return Base64.getEncoder().encodeToString(pngData); // Return QR Code as Base64 string
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
