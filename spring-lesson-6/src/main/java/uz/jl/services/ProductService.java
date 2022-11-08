package uz.jl.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import uz.jl.configs.security.UserDetails;
import uz.jl.domains.*;
import uz.jl.dto.CommentDto;
import uz.jl.dto.ProductCreateDto;
import uz.jl.dto.UserContactDto;
import uz.jl.manager.ProductMapper;
import uz.jl.repository.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper mapper;
    private final ProductRepository repository;
    private final CommentRepository commentRepository;

    private final FileStorageService service;

    private final BasketRepository basketRepository;
    private final UserContactRepository userContactRepository;

    private final ProductContactRepository productContactRepository;




    public Page<Product> findAllByPage(Optional<Integer> pageOptional, Optional<Integer> limitOptional) {
        int page = pageOptional.orElse(0);
        int size = limitOptional.orElse(6);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return repository.findAll(pageable);
    }




    public void add(ProductCreateDto dto, UserDetails userDetails, CommonsMultipartFile file) {
        Timestamp presentTime = Timestamp.valueOf(LocalDateTime.now());

//        int size = repository.findAll().size();
        Uploads upload = service.upload(file);

        Product product = mapper.fromCreateDto(dto);
        product.setFile(upload);
        product.setCreatedAt(presentTime);
        product.setCreatedBy(userDetails.getUsername());

//        Product product = Product
//                .builder()
//                .id((long) size+1)
//                .category(dto.getCategory())
//                .name(dto.getName())
//                .price(dto.getPrice())
//                .createdAt(presentTime)
//                .createdBy(userDetails.getUsername())
//                .file(upload)
//                .build();

        repository.save(product);

    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Page<Product> getAllProducts(String searchQuery, Optional<Integer> pageOptional, Optional<Integer> limitOptional){
        int page = pageOptional.orElse(0);
        int size = limitOptional.orElse(6);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return repository.getAll(searchQuery.toLowerCase(), pageable);
    }


    public void leaveComment(CommentDto dto,UserDetails userDetails) {
        int size = commentRepository.findAll().size();
        Comment comment = Comment
                .builder()
                .body(dto.getBody())
                .product_id(dto.getId())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .createdBy(userDetails.getUsername())
                .build();
        commentRepository.save(comment);

    }

    public List<Comment> findAllComments() {
        return commentRepository.findAll();
    }

    public Product getById(String id) {
        Product product = repository.findById(Long.valueOf(id)).orElseThrow();
        return product;
    }

    public void addToBasket(String id, String username) {
        Basket basket = Basket
                .builder()
                .product_id(Long.valueOf(id))
                .username(username)
                .status("not_confirmed")
                .build();
        basketRepository.save(basket);
    }

    public List<Product> findAllProductsByUsername(String username) {

        List<Product> productList = new ArrayList<>();
        List<Long> product_id_list = new ArrayList<>();
        List<Basket> baskets = basketRepository.findAll();
        for (Basket basket : baskets) {
            if(basket.getUsername().equals(username)){
                product_id_list.add(basket.getProduct_id());
            }
        }
        List<Product> allProducts = repository.findAll();
        for (Long product_id : product_id_list) {
            for (Product allProduct : allProducts) {
                if(product_id.equals(allProduct.getId())){
                    productList.add(allProduct);
                }
            }
        }

        return productList;

    }

    public void addToConfirmProduct(String id, String username) {
        Basket basket = Basket
                .builder()
                .product_id(Long.valueOf(id))
                .username(username)
                .status("confirmed")
                .build();
        basketRepository.save(basket);
    }

    public void confirmProduct(UserContactDto userContactDto) {
        UserContact userContact = UserContact
                .builder()
                .phoneNumber(userContactDto.getPhoneNumber())
                .username(userContactDto.getUsername())
                .build();
        basketRepository.updateStatus(userContact.getUsername());
        userContactRepository.save(userContact);
    }


    public void deleteProductById(String id) {
        repository.deleteById(Long.valueOf(id));
    }

    public List<Product> listAll(String keyword) {
        if (keyword != null) {
            return repository.search(keyword);
        }
        return repository.findAll();
    }

    public void saveContact(String phoneNumber, String username) {
        UserContact userContact = UserContact
                .builder()
                .username(username)
                .phoneNumber(phoneNumber)
                .build();
        userContactRepository.save(userContact);
    }

    public List<ProductContact> getAllProductContactList() throws JsonProcessingException {
        String all = productContactRepository.findAllProductContact();
        ObjectMapper m = new ObjectMapper();
        List<ProductContact> products = m.readValue(all, new TypeReference<>() {
        });
        return products;
    }


    public void updateProduct(ProductCreateDto dto,UserDetails userDetails, CommonsMultipartFile file,Product product) {

        Uploads upload = service.upload(file);
        Product product1 = Product
                .builder()
                .id(product.getId())
                .price(dto.getPrice())
                .name(dto.getName())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .file(upload)
                .createdBy(userDetails.getUsername())
                .category(dto.getCategory())
                .build();
        repository.save(product1);
//        product.setCategory(dto.getCategory());
//        product.setCreatedBy(dto.getCategory());
//        product.setName(dto.getName());
//        product.setFile(upload);
//        product.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

    }
}
