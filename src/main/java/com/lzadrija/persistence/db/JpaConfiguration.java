/**
 * Contains classes for manipulation of the validated data stored in the
 * database.
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
 * Sets up the JPA related configuration (where the used persistence provider is
 * Hibernate) for the CRUD repository used for manipulation of validated file
 * that is persisted in the database.
 * 
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
	 * Creates a {@code DataSource}, which is a factory for connections to the
	 * physical data source that this DataSource object represents. The
	 * configuration of the physical data source that is used is loaded from the
	 * propeties file. For example, it can be used for creation of connections
	 * to a PostgreSQL database.
	 * 
	 * @return data source object used for creating connections to the physical
	 *         data source
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
	 * Creates and returns an {@code EntityManagerFactory} that is used for
	 * creation of {@code EntityManagers}.
	 * 
	 * @return entity manager factory
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
	 * Creates an {@code EntityManager} using the given
	 * {@code EntityManagerFactory}. {@code EntityManager} manages the state and
	 * life cycle of entities as well as quering entities with the persistence
	 * context.
	 * 
	 * @param entityManagerFactory
	 *            used for creating application-managed {@code EntityManager}
	 * @return creating application-managed {@code EntityManager}
	 */
	@Bean
	public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
		return entityManagerFactory.createEntityManager();
	}

	/**
	 * Explicitly wires the {@code PlatformTransactionManager} that should
	 * manage transactions for the {@code EntityManagerFactory} and is to be
	 * used with the repositories being detected by the {@code repositories}
	 * element.
	 * 
	 * @return the created transaction manager
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory());
		return transactionManager;
	}

	/**
	 * Returns the instance of the {@code HibernateExceptionTranslator} that is
	 * a {@link org.springframework.dao.support.PersistenceExceptionTranslator}
	 * capable of translating {@link org.hibernate.HibernateException} instances
	 * to {@link org.springframework.dao.DataAccessException} hierarchy.
	 * 
	 * @return created exception translator
	 */
	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator() {
		return new HibernateExceptionTranslator();
	}

	/**
	 * Key for retrieving the database speciffic properties.
	 * 
	 * @author lzadrija
	 * 
	 */
	private enum DbProperty {

		/**
		 * The key for retrieving the JDBC driver.
		 */
		DRIVER("db.driver"),
		/**
		 * The key for retrieving the URL of the database in which the
		 * validation results are stored.
		 */
		URL("db.url"),
		/**
		 * The key for retrieving the username used for connecting to the
		 * database.
		 */
		USERNAME("db.username"),
		/**
		 * The key for retrieving the password used for connecting to the
		 * database.
		 */
		PASSWORD("db.password");

		private final String propName;

		/**
		 * Constructor.
		 * 
		 * @param propertyName
		 *            propName of the database configuration property
		 */
		private DbProperty(String propertyName) {
			this.propName = propertyName;
		}

		/**
		 * Returns the key used for retrieval of the database property.
		 * 
		 * @return key to retrieve the database property
		 */
		public String getName() {
			return propName;
		}

	}

}
