## Technology choices

### Java
Java was chosen as the primary language, specifically moving away from initial considerations of JavaScript/Node.js.
This decision was based on several critical factors:

*   **Robust Type System and Safety:** Unlike JavaScript's dynamic typing, Java’s strong, static type system is essential for financial applications. It ensures that complex data structures (such as account details and transaction records) are validated at compile-time, significantly reducing runtime errors and improving the overall reliability of the API.
*   **Industry Standardization:** Our analysis of existing Open Banking implementations showed that Java is the industry standard. Aligning with this ecosystem allows us to leverage battle-tested libraries for OAuth2, OIDC, and mTLS, which are more mature and standardized in the Java world than in the JavaScript ecosystem.
*   **Performance in Cryptography:** Open Banking requires intensive cryptographic operations (signing and encryption). Java’s superior support for multi-threading allows these CPU-heavy tasks to be handled in a non-blocking manner. In contrast, JavaScript’s single-threaded nature can lead to event-loop lag when performing identical operations under high load.
*   **Maintainability and Education:** Java is a cornerstone of Business Computer Science education. This ensures a steady supply of developers who are trained in both strict architectural patterns and the financial domain logic required for this project.

### Maven
We chose Maven as the build system because we have more experience with it compared to Gradle. It provides a highly structured and predictable build lifecycle, which is beneficial for ensuring consistent deployments in a security-sensitive environment.

### Javalin
Javalin was chosen over heavier frameworks like Spring or even dynamic Node.js frameworks for its "unopinionated" yet structured approach:
*   **Lightweight Nature:** We determined that the primary complexity of this API lies in the security and consent flows, not in the web framework itself. Javalin allows us to keep the boilerplate minimal.
*   **Simplicity:** By avoiding the heavy "magic" of Spring, we ensure that the security implementation is explicit and easy to audit, which is a requirement for financial compliance.

