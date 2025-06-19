import smtplib
import os
from email.message import EmailMessage

def send_email():
    smtp_server = 'smtp.gmail.com'
    smtp_port = 587
    smtp_user = os.getenv('SMTP_USER')       # your Gmail address
    smtp_pass = os.getenv('SMTP_PASS')       # your Gmail App Password
    email_to = os.getenv('EMAIL_TO')         # recipient email address

    msg = EmailMessage()
    msg['Subject'] = 'Trivy Vulnerability Report'
    msg['From'] = smtp_user
    msg['To'] = email_to
    msg.set_content('Please find the attached Trivy vulnerability report.')

    # Attach the JSON report file
    with open('trivy-report.json', 'rb') as f:
        msg.add_attachment(f.read(), maintype='application', subtype='json', filename='trivy-report.json')

    try:
        with smtplib.SMTP(smtp_server, smtp_port) as server:
            server.starttls()                # Secure connection
            server.login(smtp_user, smtp_pass)
            server.send_message(msg)
        print("Email sent successfully!")
    except Exception as e:
        print(f"Failed to send email: {e}")

if __name__ == "__main__":
    send_email()
