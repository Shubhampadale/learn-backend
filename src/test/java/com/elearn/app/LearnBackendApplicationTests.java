package com.elearn.app;

import com.elearn.app.config.security.JwtUtil;
import com.elearn.app.repositories.CategoryRepo;
import com.elearn.app.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class LearnBackendApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private CategoryService categoryService;

	//org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.elearn.app.entities.Category.courses: could not initialize proxy - no Session
	//to resolve above issue we have declare the @Transactional annotation above the addCourseToCategory method in categoryServiceImpl
	// As transactional maintains the session
	@Test
	public void testCategoryCourseRelation(){

		categoryService.addCourseToCategory("b2dc2c9f-bfb2-4876-b77b-e108d964dd10","9b2f92f9-26a3-4fe3-aef5-9495877d9da2");
	}

	@Autowired
	private CategoryRepo categoryRepo;

	//below test method return the how many courses are availale under the single category
	//here we have used the @Transactional annotation becuase without this container giving the error of No session
	//as while finding the id session gets established and closed and while performing the getcourse list there is no session
	// and hence it throws the error as "org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.elearn.app.entities.Category.courses: could not initialize proxy - no Session"
	@Test
	@Transactional
	public void testRelation(){

		int size = 	categoryRepo.findById("b2dc2c9f-bfb2-4876-b77b-e108d964dd10").get().getCourses().size();
		System.out.println(size);
	}

	@Autowired
	private JwtUtil jwtUtil;
	@Test
	public void testJwt(){

		System.out.println("testing JWT");

		String token = jwtUtil.generateToken("Shubham");
		System.out.println("The generated token is: "+token);

		System.out.println(jwtUtil.validateToken(token,"Shubham"));

		System.out.println(jwtUtil.extractUsername(token));
	}
}
