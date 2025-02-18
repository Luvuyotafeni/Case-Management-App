# Case Management App

## Overview
The Case Management App is a web-based application designed to streamline legal case handling by providing an intuitive platform for users, lawyers, and administrators. Built using **React.js**, **Spring Boot**, and **MySQL**, and hosted on **Render**, this application ensures secure and efficient case management.

## Features
### 1. User Roles
- **Admin**: Full access to all functionalities.
- **Lawyer**: Can manage assigned cases but cannot access other lawyers' cases unless approved by the admin.
- **User**: Can submit cases, book consultations, upload documents, and update their profile.

### 2. Case Management
- Users can submit cases by providing:
  - Case number
  - Station name
  - Province
  - Officer details (name & contact info)
- Admin can accept cases and assign them to lawyers based on availability and specialization.
- Lawyers can update case status at various stages and request additional documents from users.

### 3. Document Management
- Users can upload case-related documents, including:
  - Identification documents
  - Case-related files
- Documents are accessible to all involved parties.
- Only Admins and Lawyers can delete documents.
- All documents are downloadable.

### 4. User Account Management
- Users can update email, contact details, and profile pictures.
- Password reset and email verification are required for account security.
- Optional **two-step verification** can be enabled under the settings page.

## Tech Stack
- **Frontend**: React.js
- **Backend**: Spring Boot
- **Database**: MySQL
- **Hosting**: Render

## Security & Authentication
- Email verification during sign-up
- Password reset functionality
- Two-step verification (optional for users)

## Deployment
The application will be deployed on **Render**, ensuring scalability and high availability.

## Future Enhancements
- Real-time notifications for case updates
- Chat functionality between users and lawyers
- AI-powered case suggestions based on legal precedents

## License
This project is licensed under [MIT License](LICENSE).

## Contact
For inquiries or support, please reach out to [Your Contact Info].

