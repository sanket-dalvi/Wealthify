package com.sanket.wealthify.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.sanket.wealthify.swagger")
public class OpenApiConfigurationProperties {

	private Properties properties = new Properties();

	@Data
	public class Properties {
		private String title;
		private String description;
		private String apiVersion;

		private Contact contact = new Contact();
		private Security security = new Security();

		@Data
		public class Contact {
			private String email;
			private String name;
			private String url;
		}

		@Data
		public class Security {
			private String name;
			private String scheme;
			private String bearerFormat;
		}
	}

}