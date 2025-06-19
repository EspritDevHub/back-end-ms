import os
import smtplib
from email.message import EmailMessage

def send_email():
    smtp_server = 'smtp.gmail.com'
    smtp_port = 587

    smtp_user = os.getenv('SMTP_USER')
    smtp_pass = os.getenv('SMTP_PASS')
    email_to = os.getenv('EMAIL_TO')

    if not smtp_user or not smtp_pass or not email_to:
        print("Error: SMTP_USER, SMTP_PASS, and EMAIL_TO environment variables must be set.")
        return

    msg = EmailMessage()
    msg['Subject'] = 'Trivy Vulnerability Report (HTML)'
    msg['From'] = smtp_user
    msg['To'] = email_to
    msg.set_content('Please view this email in an HTML-capable client.')

    try:
        with open('trivy-report.html', 'r', encoding='utf-8') as f:
            html_content = f.read()
            msg.add_alternative(html_content, subtype='html')
    except FileNotFoundError:
        print("Error: trivy-report.html file not found.")
        return

    try:
        with smtplib.SMTP(smtp_server, smtp_port) as server:
            server.starttls()
            server.login(smtp_user, smtp_pass)
            server.send_message(msg)
        print("Email sent successfully!")
    except Exception as e:
        print(f"Failed to send email: {e}")

if __name__ == "__main__":
    send_email()
