# 🛒 E-Commerce Web Application

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-HTML5%20Engine-green.svg)](https://www.thymeleaf.org/)
[![Docker](https://img.shields.io/badge/Docker-Multi--stage-blue.svg)](https://www.docker.com/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-Ready-326ce5.svg)](https://kubernetes.io/)
[![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-red.svg)](https://www.jenkins.io/)

A complete, modern, responsive **E-Commerce Web Application** built using **Java 21**, **Spring Boot 3**, and **Thymeleaf**. 

This application uses an **in-memory product catalog** and **HTTP session-based shopping cart**, requiring **zero external database setup**. It is lightweight, fast, self-contained, and tailored for demonstrating end-to-end DevOps practices: **Git, GitHub, Maven, Jenkins CI/CD, Docker, and Kubernetes**.

---

## 🌟 Key Features

- **Store Catalog**: Curated sample products across **Electronics, Clothing, Footwear, and Accessories** with ratings, review counts, and high-resolution visuals.
- **Product Details**: Comprehensive detail page with specifications, in-stock badges, and related product recommendations.
- **Interactive Shopping Cart**:
  - Live quantity adjustment (`+` / `-`)
  - Item removal and complete cart clearing
  - Dynamic pricing recalculations (Subtotal, 8% Tax, and Shipping)
  - **Free Shipping Threshold**: Automatic free shipping discount for orders over $50 with real-time progress indicators.
- **Client-Side & Server-Side Filtering**:
  - Instant live keyword filtering across product names and descriptions.
  - Category pill filter navigation.
- **Seamless User Experience**:
  - Responsive design optimized for desktop, tablet, and mobile browsers.
  - Toast notifications when adding items to cart.
  - Cart counter badge with bounce animation in header.
  - Built-in SVG gradient image fallbacks ensuring reliable offline rendering.
- **Mock Checkout**: Instant order confirmation receipt with summary and generated order number.
- **DevOps Ready**:
  - Automated JUnit 5 unit tests for continuous integration.
  - Optimized multi-stage `Dockerfile` (Alpine JRE 21 with non-root security).
  - Declarative `Jenkinsfile` for CI/CD automation.
  - Production-ready Kubernetes `Deployment` (2 replicas, liveness/readiness probes) and `Service` (`NodePort 30080`).

---

## 🏗️ Architecture & Project Structure

```
devops-ecommerce/
├── src/
│   ├── main/
│   │   ├── java/com/example/ecommerce/
│   │   │   ├── EcommerceApplication.java        # Spring Boot entry point
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java          # Catalog & homepage routes
│   │   │   │   ├── ProductController.java       # Product details & related
│   │   │   │   └── CartController.java          # Cart management & REST APIs
│   │   │   ├── model/
│   │   │   │   ├── Product.java                 # Product domain model
│   │   │   │   ├── CartItem.java                # Line item with subtotal
│   │   │   │   └── ShoppingCart.java            # Cart totals, shipping, tax
│   │   │   └── service/
│   │   │       ├── ProductService.java          # In-memory product repository
│   │   │       └── CartService.java             # Session-backed cart management
│   │   └── resources/
│   │       ├── application.properties           # Server & template config
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css                # Pure CSS3 styling (no frameworks)
│   │       │   └── js/
│   │       │       └── main.js                  # Vanilla JS (AJAX, live filters, toasts)
│   │       └── templates/
│   │           ├── fragments/
│   │           │   └── layout.html              # Reusable header, footer & nav
│   │           ├── index.html                   # Storefront & catalog grid
│   │           ├── product-detail.html          # Individual product showcase
│   │           ├── cart.html                    # Cart table & order summary
│   │           └── checkout-success.html        # Order receipt confirmation
│   └── test/
│       └── java/com/example/ecommerce/
│           ├── EcommerceApplicationTests.java   # Spring Context validation
│           ├── ProductServiceTests.java         # Catalog & search test suite
│           └── CartServiceTests.java            # Cart math & shipping test suite
├── k8s/
│   ├── deployment.yaml                          # K8s Deployment (2 pods, probes)
│   └── service.yaml                             # K8s NodePort Service (Port 30080)
├── Dockerfile                                   # Multi-stage container build
├── .dockerignore                                # Docker build exclusions
├── Jenkinsfile                                  # Jenkins Declarative CI/CD pipeline
├── pom.xml                                      # Maven build descriptor
└── README.md                                    # Documentation & DevOps guide
```

---

## 🚀 Getting Started

### Prerequisites

- **Java Development Kit (JDK) 21** or higher
- **Apache Maven 3.9+** (or use system Maven)
- Web browser (Chrome, Firefox, Safari, Edge)

Check your installed versions:
```bash
java -version
mvn -version
```

---

## 💻 Running the Application

### Option 1: Run with Spring Boot Maven Plugin (Quickest)

```bash
mvn spring-boot:run
```

Once started, open your browser and navigate to:
```
http://localhost:8080
```

### Option 2: Build Executable JAR and Run

1. Build and package the project:
   ```bash
   mvn clean package
   ```
2. Run the generated fat JAR:
   ```bash
   java -jar target/devops-ecommerce-1.0.0.jar
   ```
3. Open `http://localhost:8080` in your browser.

### Option 3: Run Unit Tests

Execute the automated test suites:
```bash
mvn test
```

---

## 🐳 Docker Containerization

The repository provides a secure, multi-stage `Dockerfile`:
- **Stage 1 (Builder)**: Compiles the source and packages the JAR with Maven & Temurin JDK 21.
- **Stage 2 (Runtime)**: Runs on a slim Alpine Linux JRE 21 under an unprivileged `appuser`.

### Build the Docker Image
```bash
docker build -t devops-ecommerce:1.0.0 .
```

### Run the Container
```bash
docker run -d -p 8080:8080 --name ecommerce-app devops-ecommerce:1.0.0
```

### Inspect Container Logs
```bash
docker logs -f ecommerce-app
```

Stop and remove container:
```bash
docker stop ecommerce-app && docker rm ecommerce-app
```

---

## ☸️ Kubernetes Deployment

Deploy the application to a local Kubernetes cluster (Minikube, Kind, Docker Desktop, or cloud K8s):

```bash
# 1. Apply Deployment and Service
kubectl apply -f k8s/

# 2. Verify pods and services
kubectl get pods -l app=devops-ecommerce
kubectl get service devops-ecommerce-service

# 3. Access the application
# On Minikube:
minikube service devops-ecommerce-service

# On Docker Desktop / Localhost:
# Navigate to http://localhost:30080
```

To clean up Kubernetes resources:
```bash
kubectl delete -f k8s/
```

---

## 🔄 Jenkins CI/CD Pipeline

The included `Jenkinsfile` defines an enterprise-grade declarative pipeline with 6 stages:

1. **Checkout**: Pulls code from Git.
2. **Compile**: Validates syntax and compiles classes (`mvn compile`).
3. **Unit Tests**: Executes automated test suites (`mvn test`) and publishes test reports.
4. **Package**: Generates production-ready executable Fat JAR (`mvn package`).
5. **Docker Build & Tag**: Builds and tags container images with the current build number.
6. **Deploy (K8s)**: Applies Kubernetes manifests and performs rolling update verification.

---

## 📋 DevOps Case Study Guide

This application provides ideal talking points for technical presentations and case studies:

| DevOps Area | How It Is Demonstrated in This Project |
|-------------|----------------------------------------|
| **Version Control (Git)** | Clean repository structure, atomic commits, `.gitignore` tailored for Java/Spring. |
| **Build Automation (Maven)** | Multi-phase lifecycle (`compile`, `test`, `package`), dependency resolution, reproducible builds. |
| **Continuous Integration (CI)** | Automated JUnit 5 testing in Maven and automated Jenkins pipeline stages. |
| **Containerization (Docker)** | Multi-stage build patterns reducing image size, Alpine base image, non-root user execution. |
| **Continuous Delivery (CD)** | Automatic image tagging and push simulation in `Jenkinsfile`. |
| **Orchestration (Kubernetes)** | Zero-downtime rolling updates, resource boundaries (requests/limits), liveness and readiness health checks. |
| **Microservice Readiness** | Stateless HTTP architecture with pluggable session handling, ready to externalize sessions or attach Redis. |

---

## 📄 License

This project is open-source and intended for academic, educational, and DevOps demonstration purposes.
