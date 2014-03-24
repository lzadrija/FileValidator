/**
 * 
 */
package com.lzadrija.persistence.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author lzadrija
 * 
 */
@Configuration
@EnableTransactionManagement
@PropertySource("classpath:JpaConfiguration.properties")
@ComponentScan("com.lzadrija")
@EnableJpaRepositories(basePackages = "com.lzadrija.persistence.db")
public class JpaConfiguration {

	private static final String ENTITYMANAGER_PACKAGE_TO_SCAN = "com.lzadrija.persistence.db.model";

	@Autowired
	private Environment env;

	/**
	 * 
	 * @return
	 */
	@Bean
	public DataSource dataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(env.getRequiredProperty(DbProperty.DRIVER.getName()));
		dataSource.setUrl(env.getRequiredProperty(DbProperty.URL.getName()));
		dataSource.setUsername(env.getRequiredProperty(DbProperty.USERNAME.getName()));
		dataSource.setPassword(env.getRequiredProperty(DbProperty.PASSWORD.getName()));

		return dataSource;
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public EntityManagerFactory entityManagerFactory() {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean emFactoryBean = new LocalContainerEntityManagerFactoryBean();
		emFactoryBean.setJpaVendorAdapter(vendorAdapter);
		emFactoryBean.setPackagesToScan(ENTITYMANAGER_PACKAGE_TO_SCAN);
		emFactoryBean.setDataSource(dataSource());
		emFactoryBean.afterPropertiesSet();

		return emFactoryBean.getObject();
	}

	/**
	 * 
	 * @param entityManagerFactory
	 * @return
	 */
	@Bean
	public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
		return entityManagerFactory.createEntityManager();
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory());
		return transactionManager;
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

	/**
	 * 
	 * @author lzadrija
	 * 
	 */
	private enum DbProperty {

		DRIVER("db.driver"),
		URL("db.url"),
		USERNAME("db.username"),
		PASSWORD("db.password");

		private final String name;

		/**
		 * 
		 * @param propertyName
		 */
		private DbProperty(String propertyName) {
			this.name = propertyName;
		}

		public String getName() {
			return name;
		}

	}

}
