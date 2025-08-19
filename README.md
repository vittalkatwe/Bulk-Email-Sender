# 📧 Bulk Email Sender (Spring Boot)

A simple yet powerful **Bulk Email Sender** application built with **Spring Boot**.  
It allows you to send bulk emails by providing:

- **Option 1:** Your email address + app password  
- **Option 2:** OAuth authentication (recommended for Gmail & other providers)  

You just need to upload a **CSV file** containing recipients and email details.

---

## 🚀 Features
- Send bulk emails using **SMTP** with Spring Boot.  
- Supports **two authentication modes**:
  - Email + App Password  
  - OAuth (Google, Microsoft, etc.)  
- Accepts a **CSV file input** with recipient email, subject, and body.  
- Simple, easy-to-use REST endpoints.  

---

## 📂 CSV File Format
Prepare a CSV file in the following format:

```csv
email,subject,body
test1@example.com,Welcome!,Hello Test1, welcome to our service.
test2@example.com,Reminder,This is a gentle reminder for tomorrow's meeting.
