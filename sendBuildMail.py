import os
import sys
import smtplib
import subprocess
from email.message import EmailMessage

def get_current_time():
    try:
        return subprocess.check_output(['date']).decode('utf-8').strip()
    except Exception:
        return "Unknown"

def send_email():
    smtp_server = 'smtp.gmail.com'
    smtp_port = 587

    smtp_user = os.getenv('SMTP_USER')
    smtp_pass = os.getenv('SMTP_PASS')
    email_to = os.getenv('EMAIL_TO')
    email_cc = os.getenv('EMAIL_CC', '')  # Optional
    build_status = os.getenv('BUILD_STATUS', '').lower()

    if not smtp_user or not smtp_pass or not email_to or not build_status:
        print("❌ Error: SMTP_USER, SMTP_PASS, EMAIL_TO, and BUILD_STATUS environment variables must be set.")
        sys.exit(1)

    if build_status not in ['success', 'fail']:
        print("❌ Invalid BUILD_STATUS. Use 'success' or 'fail'.")
        sys.exit(1)

    is_success = build_status == 'success'
    subject = "✅ Build Success Notification" if is_success else "❌ Build Failed Notification"
    color = "green" if is_success else "red"
    emoji = "🎉" if is_success else "💥"
    message = (
        "Your application build has been completed without errors."
        if is_success else
        "Your application build has failed. Please check the logs."
    )

    msg = EmailMessage()
    msg['Subject'] = subject
    msg['From'] = smtp_user
    msg['To'] = email_to
    if email_cc:
        msg['Cc'] = email_cc

    msg.set_content('Your email client does not support HTML.')

    html_content = f"""
    <html>
      <body>
        <h2 style="color:{color};">{emoji} {subject}</h2>
        <p>{message}</p>
        <p><strong>Time:</strong> {get_current_time()}</p>
      </body>
    </html>
    """

    msg.add_alternative(html_content, subtype='html')

    try:
        with smtplib.SMTP(smtp_server, smtp_port, timeout=10) as server:
            server.starttls()
            server.login(smtp_user, smtp_pass)
            server.send_message(msg)
        print("✅ Email sent successfully!")
    except Exception as e:
        print(f"❌ Failed to send email: {e}")
        sys.exit(1)

    # Fail the script if build failed
    if not is_success:
        sys.exit(1)

if __name__ == "__main__":
    send_email()
