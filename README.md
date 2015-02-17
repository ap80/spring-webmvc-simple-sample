#README
Di contoh project ini kita akan membuat aplikasi web java (servlet base) menggunakan Spring Framework atau lebih tepatnya menggunakan Spring Web MVC Framework. Harapannya contoh project web ini akan menjadi dasar untuk contoh berikutnya.

##Prerequisite
Berikut adalah kebutuhan dasar untuk runing contoh project ini

1. Java 7+
2. Koneksi internet untuk resolve dan download library-lubrary yg jadi dependencies.
3. Git (Optional)
4. Spring Tool Suite IDE (Optional, bisa diganti dengan IDE lain yang support Maven, misal NetBeans)

##Running
###Terminal
Persyaratan tambahan, install [apache maven](maven.apache.org) dan daftarkan `MAVEN_HOME/bin` directory sebagai environment variable, daripada manage dependencies dan build process secara manual. Untuk running contoh ini via terminal, dari project folder sebagai working directory, eksekusi perintah berikut

```
mvn clean
mvn jetty:run
```

Sebagai catatan, bisa jadi perintah ini akan makan waktu (tergantung kecepatan internet) jika library yang dibutuhkan belum ada di local maven repo Anda.

###Eclipse

Saya sarankan menggunakan String Source Tool Suite, import project (Existing maven project), proses import mungkin akan makan waktu lama karena maven akan mengunduh library dependecies dari central repo maven. Lalu pilih 'Run As' -> 'Maven build...' dan ketik Goals 'clean jetty:run'

Selain itu bisa juga dengan, Run on Server. Pada Tomcat 8 atau 'turunannya' seharusnya ga ada masalah. Jika menggunakan Tomcat 7 atau 'turunannya', ganti versi library servlet-api di pom.xml menjadi 3.0.1.

###Lihat Browser
Arahkan browser anda ke alamat [localhost](http://localhost:8080). 

##How To
Asumsi yang saya gunakan adalah Anda menggunakan IDE Sprin Tools Suite, jika tidak saya sarankan untuk mengunduhnya sebelum memulai.

###Buat Proyek Maven
Sebagai sedikit pengantar jika Anda belum bersahabat dengan Maven, Maven merupakan tools yang berguna untuk dua aspek dalam development software, [*dependency management*](http://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html) dan [*build process management*](http://maven.apache.org/guides/introduction/introduction-to-plugins.html). Setiap proyek maven menggunakan file konfigurasi [`pom.xml`](http://maven.apache.org/guides/introduction/introduction-to-the-pom.html), disinilah kedua aspek tersebut diatur. Mekanisme *dependency management* diterapkan menggunakan [*repository*](http://maven.apache.org/guides/introduction/introduction-to-repositories.html). Sementara *build process management* dilakukan bertahap dalam [lifecycle](http://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html) yang dibagi menjadi *phases*, di dalam masing-masing *phase* inilah developer dapat menggunakan [*plugin*](http://maven.apache.org/guides/introduction/introduction-to-plugins.html) untuk memanipulasi proses build sesuai dengan kebutuhan atau *goal* yang diinginkan. Daftar plugin yang terdaftar dalam situs resmi maven dapat dilihat [di sini](http://maven.apache.org/plugins/index.html) akan tetapi masih banyak plugin lain yang disediakan oleh pihak ketiga, misalnya plugin compiler groovy atau aspectj.

Langkah pertama adalah create maven web project (jenis *packaging* war), untuk contoh ini saya menggunakan groupId project `com.innovez.samples` dan artifactId `spring-webmvc-sample`, Anda bebas memilih kedua nilai ini sesuai. Lalu edit file `pom.xml` sehingga berisi seperti xml berikut ini.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.innovez.samples</groupId>
	<artifactId>spring-webmvc-sample</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>war</packaging>
	<name>Spring WebMVC Sample</name>
	<description>Creating simplest java web application using spring-webmvc</description>

	<dependencies>
		<!-- 
		Dependecy untuk logging, facade logging yang akan mendelegasikan tugasnya ke log4j
		-->
		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-log4j12</artifactId>
			<version>1.7.10</version>
		</dependency>

		<!--
		Library utama untuk membuat web application dengan spring 
		-->
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-webmvc</artifactId>
			<version>4.1.4.RELEASE</version>
		</dependency>
		<!-- 
		Silahkan ganti versi servlet-api sesuai kontainer yang di gunakan. 
		Library ini seharusnya disedialan oleh servlet container pada saat runtime.
		-->
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>3.1.0</version>
			<scope>provided</scope>
		</dependency>
		<!-- 
		Library yang akan digunakan oleh komponen internal spring web untuk 
		serialize object java ke json 
		-->
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.5.1</version>
		</dependency>

		<!-- 
		Impleentasi bean validation ini diperlukan untuk validasi bean, termasuk 
		pada saat binding request di controller. Melengkapi fitur yang sudah diberikan 
		fitur internal spring utuk validation. 
		-->
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-validator</artifactId>
			<version>5.1.3.Final</version>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.2</version>
				<configuration>
					<source>1.7</source>
					<target>1.7</target>
					<encoding>UTF-8</encoding>
				</configuration>
			</plugin>
			<!-- 
			Embedded jetty servlet container, web project bisa dijalankan dengan perintah berikut 
			mvn jetty:run -->
			<plugin>
				<groupId>org.eclipse.jetty</groupId>
				<artifactId>jetty-maven-plugin</artifactId>
				<version>9.2.7.v20150116</version>
			</plugin>
		</plugins>
	</build>
</project>
```

###Konfigurasi Deskripsi Deployment
Selanjutnya kita melakukan konfigurasi untuk aplikasi web kita, cara tradisional dan sampai sekarang masih dipake adalah menggunakan file *web deployment descriptor* `web.xml`.