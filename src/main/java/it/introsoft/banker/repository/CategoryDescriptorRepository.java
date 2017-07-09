package it.introsoft.banker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(path = "categories-descriptors", collectionResourceRel = "categoriesDescriptors")
public interface CategoryDescriptorRepository extends JpaRepository<CategoryDescriptor, Long>, QueryDslPredicateExecutor<CategoryDescriptor> {
}
