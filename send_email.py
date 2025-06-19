import smtplib
from email.message import EmailMessage

def send_email():
    smtp_server = 'smtp.gmail.com'
    smtp_port = 587

    smtp_user = 'jobjob.bs2@gmail.com'
    smtp_pass = 'crzg nunr jtge'  # your actual app password here, no spaces!
    email_to = 'jobjob.bs2@gmail.com'

    msg = EmailMessage()
    msg['Subject'] = 'Trivy Vulnerability Report (HTML)'
    msg['From'] = smtp_user
    msg['To'] = email_to
    msg.set_content('Please find the attached Trivy vulnerability HTML report.')

    try:
        with open('trivy-report.html', 'rb') as f:
            html_data = f.read()
            msg.add_attachment(html_data, maintype='text', subtype='html', filename='trivy-report.html')
    except FileNotFoundError:
        print("Error: trivy-report.html file not found.")
        return

    try:
        with smtplib.SMTP(smtp_server, smtp_port) as server:
            server.starttls()  # Secure the connection
            server.login(smtp_user, smtp_pass)
            server.send_message(msg)
        print("Email sent successfully!")
    except Exception as e:
        print(f"Failed to send email: {e}")

if __name__ == "__main__":
    send_email()
