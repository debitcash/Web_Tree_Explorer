### The web scraping tool

This project is a Java-based web application designed to extract and analyze information from websites. It leverages JSF for the user interface, JPA for data persistence, and Jsoup for web scraping.

Key Features:
- Provides an intuitive web interface for users to input target URLs and parameters.
- Employs a depth-first search algorithm to explore and analyze website structure.
- Extracts relevant information such as phone numbers, emails, images, and PDFs.
- Presents extracted data in a hierarchical format for easy analysis.
- Stores analysis results in a database for future reference.

Technologies Used:

- **Java**: Core programming language.
- **Jakarta EE**: Provides the platform for web application development.
- **JSF**: Builds the user interface.
- **JPA**: Manages data persistence.
- **Jsoup**: Parses HTML content for data extraction.
- **Apache OpenJPA**: Implements the JPA specification.
- **PrimeFaces**: Enhances the user interface with components.
- **MySQL**: Stores analysis results.

Project Structure:

Web Application: Contains JSF pages, managed beans, and configuration files for the user interface.
Business Logic: Includes classes responsible for web scraping, data processing, and algorithm implementation.
Data Access Layer: Handles database interactions using JPA.
Persistence Layer: Defines entities and their mapping to database tables.

Overall, this project offers a powerful SEO tool for analyzing website structure and identifying optimization opportunities. By visualizing URL hierarchies, users can quickly uncover internal linking issues, improve site navigation, and enhance search engine visibility.

>[!CAUTION]
> Even though the complexity of DFS is $O(E)$, where $E$ is the number of edges, the number of nodes in the tree can grow exponentially as $n^d$, where $n$ is the branching factor and $d$ is the depth. Thus, setting a depth limit is crucial to manage the exponential growth of nodes and prevent excessive computation time. While this may be manageable for small websites, it can lead to significant performance issues for larger sites. Therefore, in this implementation, the search is limited to 30 minutes to manage performance.

<br>
<div align="center">
<img src="WebScreenshots.png">
</div>
