package com.masai.app.book_review.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperClass {
        public static ModelMapper modelMapper(){
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper;
        }
}
