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
    msg['Subject'] = '✅ Build Success Notification'
    msg['From'] = smtp_user
    msg['To'] = email_to
    msg.set_content('Please view this email in an HTML-capable client.')

    html_content = """
    <html>
      <body>
        <h2 style="color:green;">🎉 Build Completed Successfully!</h2>
        <p>Your application build has been completed without errors.</p>
        <p>Time: {time}</p>
      </body>
    </html>
    """.format(time=os.popen('date').read().strip())

    msg.add_alternative(html_content, subtype='html')

    try:
        with smtplib.SMTP(smtp_server, smtp_port) as server:
            server.starttls()
            server.login(smtp_user, smtp_pass)
            server.send_message(msg)
        print("✅ Email sent successfully!")
    except Exception as e:
        print(f"❌ Failed to send email: {e}")

if __name__ == "__main__":
    send_email()
