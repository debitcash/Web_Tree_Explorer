This project is a Java-based web application designed to extract and analyze information from websites. It leverages JSF for the user interface, JPA for data persistence, and Jsoup for web scraping.

Key Features:

User-friendly interface: Provides an intuitive web interface for users to input target URLs and parameters.
Depth-first traversal: Employs a depth-first search algorithm to explore and analyze website structure.
Data extraction: Extracts relevant information such as phone numbers, emails, images, and PDFs.
Hierarchical visualization: Presents extracted data in a hierarchical format for easy analysis.
Data persistence: Stores analysis results in a database for future reference.
Technologies Used:

Java: Core programming language.
Jakarta EE: Provides the platform for web application development.
JSF: Builds the user interface.
JPA: Manages data persistence.
Jsoup: Parses HTML content for data extraction.
Apache OpenJPA: Implements the JPA specification.
PrimeFaces: Enhances the user interface with components.
Project Structure:

Web Application: Contains JSF pages, managed beans, and configuration files for the user interface.
Business Logic: Includes classes responsible for web scraping, data processing, and algorithm implementation.
Data Access Layer: Handles database interactions using JPA.
Persistence Layer: Defines entities and their mapping to database tables.
This project aims to provide a robust and efficient web scraping solution with a user-friendly interface.
