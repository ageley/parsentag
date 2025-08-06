[@parsentagbot](https://t.me/parsentagbot)

Parse N' Tag - OCR Telegram chatbot

---

## Local debug

*Prerequisites:* AWS Setup, except RDS and ECR (see below).

Create an .env file in the project root:

```
TELEGRAM_BOT_TOKEN=7001234567:Your_Telegram_Token123
AWS_ACCESS_KEY_ID=AKIAYOURACCESSKEY20C
AWS_SECRET_ACCESS_KEY=Your_Secret_Access_Key111
AWS_REGION=us-east-1
AWS_S3_IMAGE_BUCKET_NAME=parsentag-s3-image-bucket-123guid456
AWS_S3_TEXT_BUCKET_NAME=parsentag-s3-text-bucket-789guid456
```

Build a .jar from the project root:

```shell
./gradlew build
```

Run an app with a local Postgres DB:

```shell
docker compose up -d --build
```

Cleanup:

```shell
docker compose stop
```

```shell
docker compose rm --volumes
```

---

## Deploy to AWS cloud

*Prerequisites:* AWS Setup (see below).

---

### Set up AWS CLI (Command Line Interface)

*Note:* Repeat these steps on a local machine and on the EC2 instance later.

Install AWS CLI (if not already installed):

```shell
snap install aws-cli --classic
```

Check the installation:

```shell
aws --version
```

Configure credentials:

```shell
aws configure
```

- AWS Access Key ID: **AKIAYOURACCESSKEY20C**
- AWS Secret Access Key: **Your_Secret_Access_Key111**
- Default region name: **us-east-1**
- Default output format: **json**

### Push an image

Build a .jar from a project root:

```shell
./gradlew build
```

Build an image from a project root:

```shell
docker build -t parsentag:1.0 .
```

Log into a repository:

```shell
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 007123456789.dkr.ecr.us-east-1.amazonaws.com
```

Tag the image:

```shell
docker tag parsentag:1.0 007123456789.dkr.ecr.us-east-1.amazonaws.com/parsentag:1.0
```

Push the image into the repository:

```shell
docker push 007123456789.dkr.ecr.us-east-1.amazonaws.com/parsentag:1.0
```

Log out from the repository:

```shell
docker logout 007123456789.dkr.ecr.us-east-1.amazonaws.com
```

### Connect to an EC2 instance

EC2 -> **✅ parsentag-ec2** -> Connect

- EC2 Instance Connect: **Connect using Public IP**
- Username: **ec2-user**

-> Connect

### Set up Docker

```shell
sudo yum update -y
sudo yum install -y docker
sudo service docker start
sudo usermod -aG docker ec2-user
```

Log out and in again to apply changes.

### Run an app

*Prerequisites:* Set up AWS CLI (see above).

Create an .env file:

```
TELEGRAM_BOT_TOKEN=7001234567:Your_Telegram_Token123
AWS_ACCESS_KEY_ID=AKIAYOURACCESSKEY20C
AWS_SECRET_ACCESS_KEY=Your_Secret_Access_Key111
AWS_REGION=us-east-1
AWS_S3_IMAGE_BUCKET_NAME=parsentag-s3-image-bucket-123guid456
AWS_S3_TEXT_BUCKET_NAME=parsentag-s3-text-bucket-789guid456
SPRING_DATASOURCE_URL=jdbc:postgresql://parsentag-db.some123name.us-east-1.rds.amazonaws.com:5432/parsentag
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=Your_Postgres_Password333
```

Pull an image:

```shell
docker pull 007123456789.dkr.ecr.us-east-1.amazonaws.com/parsentag:1.0
```

Run an app in Docker:

//todo: Протестировать подключение к Postgres в облаке, может потребоваться дополнительная политика

```shell
docker run --rm --env-file .env --name parsentag 007123456789.dkr.ecr.us-east-1.amazonaws.com/parsentag:1.0
```

## AWS setup (Amazon Web Services)

A minimum Free Tier test configuration sample.

Substitute:
- **us-east-1** with your AWS Region
- **007123456789** with your AWS Account ID
- **parsentag-s3-image-bucket-123guid456** with your image bucket name
- **parsentag-s3-text-bucket-789guid456** with your text bucket name

*Note:* Choose a region with Amazon Textract available.

---

### S3 (Simple Storage Service)

#### *Create an image bucket*

S3 -> Create bucket

- Bucket name: **parsentag-s3-image-bucket-123guid456**
- Object Ownership: **ACLs disabled**
- Block Public Access settings for this bucket: **✅ Block all public access**
- Bucket Versioning: **Disable**
- Encryption type: **Server-side encryption with Amazon S3 managed keys (SSE-S3)**
- Bucket Key: **Enable**

#### *Set an image bucket cleanup rule*

S3 -> General purpose buckets -> **parsentag-s3-image-bucket-123guid456**

Management -> Create lifecycle rule

- Lifecycle rule name: **parsentag-s3-image-cleanup**
- Choose a rule scope: **Apply to all objects in the bucket**
  - **✅ I acknowledge**
- Lifecycle rule actions: **✅ Expire current versions of objects**
- Days after object creation: **1**

-> Create rule

#### *Create a text bucket*

S3 -> Create bucket

- Bucket name: **parsentag-s3-text-bucket-789guid456**
- Object Ownership: **ACLs disabled**
- Block Public Access settings for this bucket: **✅ Block all public access**
- Bucket Versioning: **Disable**
- Encryption type: **Server-side encryption with Amazon S3 managed keys (SSE-S3)**
- Bucket Key: **Enable**

#### *Set a text bucket cleanup rule*

S3 -> General purpose buckets -> **parsentag-s3-text-bucket-789guid456**

Management -> Create lifecycle rule

- Lifecycle rule name: **parsentag-s3-text-cleanup**
- Choose a rule scope: **Apply to all objects in the bucket**
  - **✅ I acknowledge**
- Lifecycle rule actions: **✅ Expire current versions of objects**
- Days after object creation: **1**

-> Create rule

---

### VPC (Virtual Private Cloud)

#### *Create a network*

VPC -> Create VPC

- Resources to create: **VPC and more**
- Project: **parsentag**
- IPv4 CIDR block: **15.0.0.0/24**
- IPv6 CIDR block: **No IPv6 CIDR block**
- Tenancy: **Default**
- Number of Availability Zones (AZs): **2**
- Number of public subnets: **2**
- Number of private subnets: **2**
- NAT gateways: **None**
- VPC endpoints: **S3 Gateway**

-> Create VPC

#### *Attach an S3 gateway to a public subnet*

VPC -> Endpoints -> **parsentag-vpce-s3**

Route tables -> Manage route tables

- **parsentag-rtb-private1-us-east-1a** -> ⏹️ Uncheck
- **parsentag-rtb-private2-us-east-1b** -> ⏹️ Uncheck
- **parsentag-rtb-public** -> ✅ Check

-> Modify route tables

---

### EC2 (Elastic Compute Cloud)

#### *Create a VM (Virtual Machine)*

EC2 -> Launch instance

Name and tags:

  - Name: **parsentag-ec2**

- Application and OS Images (Amazon Machine Image):

  - Amazon Machine Image (AMI): **Amazon Linux 2023 kernel-6.12**
  - Architecture: **64-bit (x86)**

Instance type: **t2.nano**

Key pair (login) -> Create new key pair

- Key pair name: **parsentag-ec2-keypair**
- Key pair type: **RSA**
- Private key file format: **.pem**

-> Create key pair

Save **parsentag-ec2-keypair.pem** file.

Network settings -> Edit

- VPC: **parsentag-vpc**
- Subnet: **parsentag-subnet-public1-us-east-1a**
- Auto-assign public IP: **Enable**
- Firewall (security groups): **Create security group**
- Security group name: **parsentag-ec2-sg**
- Inbound Security Group Rules:
  - Type: **ssh**
  - Source type: **Anywhere**
 
Configure storage:

- 1 x **8** GiB **Magnetic (standard)**

Summary:

- Number of instances: **1**

-> Launch instance

---

### RDS (Relational Database Service)

#### *Create a database*

RDS -> Create database

- Choose a database creation method: **Standard create**
- Engine options:
  - Engine type: **PostgreSQL**
  - Engine version: **PostgreSQL 17.5-R1**
- Templates: **Sandbox**
- Availability and durability: **Single-AZ DB instance deployment (1 instance)**
- Settings
  - DB instance identifier: **parsentag-db** 
- Credential Settings:
  - Master username: **postgres**
  - Credentials management: **Self managed**
  - **✅ Auto generate password**
- Instance configuration:
  - DB Instance class: **db.t3.micro**
- Storage:
  - Storage type: **Magnetic**
  - Allocated storage: **5 GiB**
- Connectivity:
  - Compute resource: **Connect to an EC2 compute resource**
  - EC2 instance: **parsentag-ec2**
  - Network type: **IPv4**
  - VPC: **parsentag-vpc**
  - DB subnet group: **Automatic setup**
  - Public access: **No**
  - VPC security group (firewall): **Create new**
  - New VPC security group name: **parsentag-db-sg**
  - Availability Zone: **us-east-1a**
  - Certificate authority: **rds-ca-rsa2048-g1 (default)**
  - Additional configuration:
    - Database port: **5432**
- Database authentication: **Password authentication**
- Monitoring
  - Database Insights: **Standard**
  - Performance Insights: **⏹️ Uncheck**
- Additional configuration:
  - Initial database name: **parsentag**
  - Enable automated backups: **⏹️ Uncheck**
  - Enable encryption: **⏹️ Uncheck**
  - Maintenance window: **No preference**
  - Enable deletion protection: **⏹️ Uncheck**

-> Create database

Wait for a green popup message and copy connection details from it:

- User: **postgres**
- Password: **Your_Postgres_Password333**
- Endpoint: **parsentag-db.some123name.us-east-1.rds.amazonaws.com**

---

### ECR (Elastic Container Registry)

#### *Create a Docker repository*

ECR -> Create

- Repository name: **parsentag**
- Image tag mutability: **Mutable**
- Encryption configuration: **AES-256**

-> Create

#### *Set a cleanup rule*

ECR -> Repositories -> **parsentag**

Lifecycle Policy -> Create rule

- Rule priority: **1**
- Rule description: **Keep only the last image**
- Image status: **Any**
- Match criteria: **Image count more than 1**

-> Save

---

### IAM (Identity and Access Management)

#### *Create an ECR policy*

IAM -> Policies -> Create policy

**1.** Specify permissions

Service: **Elastic Container Registry**

Actions allowed:

- List:
  - **✅ DescribeImages**
  - **✅ ListImages**
- Read:
  - **✅ BatchCheckLayerAvailability**
  - **✅ BatchGetImage**
  - **✅ DescribeRepositories**
  - **✅ GetDownloadUrlForLayer**
  - **✅ ListTagsForResource**
- Write:
  - **✅ BatchDeleteImage**
  - **✅ BatchImportUpstreamImage**
  - **✅ CompleteLayerUpload**
  - **✅ InitiateLayerUpload**
  - **✅ PutImage**
  - **✅ UploadLayerPart**
- Permissions management: **⏹️ Select none**
- Tagging: **✅ All tagging actions**

Resources -> Specific -> Add ARNs

- Resource in: **This account**
- Resource region: **us-east-1**
- Resource repository name: **parsentag**

-> Add ARNs

-> Next

**2.** Review and create

- Policy name: **parsentag-ecr-policy**

-> Create policy

#### *Create an S3 policy*

IAM -> Policies -> Create policy

**1.** Specify permissions

Service: **S3**

Actions allowed:

- List:
  - **✅ ListBucket**
- Read:
  - **✅ GetObject**
- Write:
  - **✅ DeleteObject**
  - **✅ PutObject**
  - **✅ PutObjectRetention**
- Permissions management: **⏹️ Select none**
- Tagging: **⏹️ Select none**

Resources -> Specific

**bucket** -> Add ARNs

- Resource bucket name: **parsentag-s3-image-bucket-123guid456**

-> Add ARNs

**bucket** -> Add ARNs

- Resource bucket name: **parsentag-s3-text-bucket-789guid456**

-> Add ARNs

**object** -> Add ARNs

- Resource bucket name: **parsentag-s3-image-bucket-123guid456**
- Resource object name: **✅ Any object name**

-> Add ARNs

**object** -> Add ARNs

- Resource bucket name: **parsentag-s3-text-bucket-789guid456**
- Resource object name: **✅ Any object name**

-> Add ARNs

-> Next

**2.** Review and create

- Policy name: **parsentag-s3-policy**

-> Create policy

#### *Create a Textract policy*

IAM -> Policies -> Create policy

**1.** Specify permissions

Service: **Textract**

Actions allowed:

- Read: **✅ DetectDocumentText**
- Write: **⏹️ Select none**
- Tagging: **⏹️ Select none**

**2.** Review and create

- Policy name: **parsentag-textract-policy**

-> Create policy

#### *Create a user*

IAM -> Users -> Create user

**1.** Specify user details

- User name: **parsentag-iam-user**

-> Next

**2.** Set permissions

- Permissions options: **Attach policies directly**
- Permissions policies:
  - **✅ parsentag-ecr-policy**
  - **✅ parsentag-s3-policy**
  - **✅ parsentag-textract-policy**

-> Next

**3.** Review and create

-> Create user

#### *Create an access key*

IAM -> Users -> **parsentag-iam-user**

Summary -> Create access key

**1.** Access key best practices & alternatives

- Use case: **Command Line Interface (CLI)**
- Confirmation: **✅ Check**

-> Next

**2.** Set description tag

- Description tag value: **parsentag-iam-user-cli**

-> Create access key

**3.** Retrieve access keys

Copy and save values from a page:

- Access key: **AKIAYOURACCESSKEY20C**
- Secret access key: **Your_Secret_Access_Key111**

Or download a .csv file.

-> Done
