# Jenkins CI/CD Setup Guide

Complete step-by-step guide to setting up Jenkins with Docker and building a Java Maven project.

---

## Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running
- [Git](https://git-scm.com/) installed
- A [GitHub](https://github.com) account
- A Java project with Maven (`pom.xml`)

---

## Part 1: Project Structure

Your Java project should follow this Maven structure:

```
calculator-app/
├── pom.xml
└── src/
    ├── main/java/com/example/
    │   └── Calculator.java
    └── test/java/com/example/
        └── CalculatorTest.java
```

### Calculator.java
```java
package com.example;

public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public double divide(int a, int b) {
        if (b == 0) throw new ArithmeticException("Cannot divide by zero");
        return (double) a / b;
    }

    public static void main(String[] args) {
        Calculator calc = new Calculator();
        System.out.println("Sum: " + calc.add(5, 3));
        System.out.println("Difference: " + calc.subtract(5, 3));
        System.out.println("Product: " + calc.multiply(5, 3));
        System.out.println("Quotient: " + calc.divide(10, 2));
    }
}
```

### CalculatorTest.java
```java
package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void testAddition() {
        Calculator calc = new Calculator();
        assertEquals(8, calc.add(5, 3));
    }

    @Test
    void testSubtraction() {
        Calculator calc = new Calculator();
        assertEquals(2, calc.subtract(5, 3));
    }

    @Test
    void testMultiplication() {
        Calculator calc = new Calculator();
        assertEquals(15, calc.multiply(5, 3));
    }

    @Test
    void testDivision() {
        Calculator calc = new Calculator();
        assertEquals(5.0, calc.divide(10, 2));
    }

    @Test
    void testDivisionByZero() {
        Calculator calc = new Calculator();
        assertThrows(ArithmeticException.class, () -> calc.divide(10, 0));
    }
}
```

### pom.xml
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>calculator-app</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.9.3</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0</version>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## Part 2: Push to GitHub

```powershell
# Initialize Git repo
git init

# Stage all files
git add .

# Commit
git commit -m "Initial commit"

# Add your GitHub remote
git remote add origin https://github.com/<your-username>/<repo-name>.git

# Rename branch to main and push
git branch -M main
git push -u origin main
```

> **Note:** In PowerShell use `;` instead of `&&` to chain commands.

---

## Part 3: Run Jenkins via Docker

1. **Open Docker Desktop** and wait for it to fully start (green status in the tray)

2. **Run Jenkins container:**
```powershell
docker run -d -p 8080:8080 -p 50000:50000 --name jenkins jenkins/jenkins:lts
```

3. **Get the initial admin password:**
```powershell
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

4. Open **http://localhost:8080** in your browser

---

## Part 4: Initial Jenkins Setup (UI)

1. **Unlock Jenkins** — paste the admin password from Step 3
2. Click **"Install suggested plugins"** and wait
3. **Create Admin User** — fill in username, password, email → **Save and Continue**
4. **Instance Configuration** — confirm `http://localhost:8080/` → **Save and Finish**
5. Click **"Start using Jenkins"**

---

## Part 5: Configure Maven in Jenkins

1. Go to **Manage Jenkins → Tools**
2. Scroll to the **Maven** section → click **"Add Maven"**
3. Set **Name** to exactly: `Maven`
4. Check **"Install automatically"** and pick the latest version
5. Click **Save**

---

## Part 6: Create the Jenkinsfile

Create a file named `Jenkinsfile` (no extension) in the **root** of your project (not inside `calculator-app`):

```groovy
pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('calculator-app') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage('Test') {
            steps {
                dir('calculator-app') {
                    sh 'mvn test'
                }
            }
        }
    }

    post {
        always {
            dir('calculator-app') {
                junit 'target/surefire-reports/*.xml'
            }
        }
    }
}
```

> **Important Notes:**
> - Use `sh` (not `bat`) because Jenkins runs inside a Linux Docker container
> - The `tools { maven 'Maven' }` name must match exactly what you set in Jenkins Tools

Push the Jenkinsfile to GitHub:
```powershell
git add Jenkinsfile
git commit -m "Add Jenkinsfile"
git push
```

---

## Part 7: Create a Jenkins Pipeline Job

1. Go to Jenkins Dashboard → click **"New Item"**
2. Enter name: `Calculator-Pipeline`
3. Select **"Pipeline"** → click **OK**
4. Scroll to the **Pipeline** section
5. Change **Definition** to **"Pipeline script from SCM"**
6. Set **SCM** to **Git**
7. Enter your **Repository URL**: `https://github.com/<your-username>/<repo-name>.git`
8. Change **Branch Specifier** from `*/master` → `*/main`
9. **Script Path** should be `Jenkinsfile` (default)
10. Click **Save**
11. Click **"Build Now"** to run your first pipeline!

---

## Part 8: Auto-Trigger Builds (Poll SCM)

To make Jenkins automatically check GitHub for changes every 5 minutes:

1. Open your pipeline job → **Configure**
2. Under **"Build Triggers"**, check **"Poll SCM"**
3. Set schedule: `H/5 * * * *`
4. Click **Save**

---

## Common Errors & Fixes

| Error | Cause | Fix |
|-------|-------|-----|
| `failed to connect to the docker API` | Docker Desktop not running | Open Docker Desktop and wait for it to start |
| `src refspec main does not match any` | Branch named `master` not `main` | Run `git branch -M main; git push -u origin main` |
| `Invalid agent type "docker"` | Docker Pipeline plugin not installed | Use `agent any` instead |
| `Batch scripts can only be run on Windows nodes` | Used `bat` instead of `sh` | Replace `bat` with `sh` in Jenkinsfile |
| `Wrong name: Calculator` | Running class without package prefix | Run as `java com.example.Calculator` from `src/main/java` |

---

## Final Project Structure

```
Jenkin Testing Project/
├── .git/
├── Jenkinsfile                          ← Pipeline definition
└── calculator-app/
    ├── pom.xml                          ← Maven build config
    └── src/
        ├── main/java/com/example/
        │   └── Calculator.java          ← Main application
        └── test/java/com/example/
            └── CalculatorTest.java      ← Unit tests
```
