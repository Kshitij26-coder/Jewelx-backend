package in.jewelx.jewelxbackend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.jewelx.jewelxbackend.dto.BrandDto;
import in.jewelx.jewelxbackend.dto.BrandResponseDto;
import in.jewelx.jewelxbackend.entity.BrandEntity;
import in.jewelx.jewelxbackend.entity.UserEntity;
import in.jewelx.jewelxbackend.exception.IdNotFoundException;
import in.jewelx.jewelxbackend.mapper.BrandMapper;
import in.jewelx.jewelxbackend.repository.BrandRepository;
import in.jewelx.jewelxbackend.service.IBrandService;
import jakarta.transaction.Transactional;

@Service
public class BrandService implements IBrandService {

	@Autowired
	private BrandRepository brandRepo;

	@Override
	@Transactional
	public BrandEntity createBrand(BrandEntity brandEntity) {
		System.out.println(brandEntity);
		if (brandEntity == null) {
			throw new NullPointerException("Brand Entity does not contains any data");
		}
		return brandRepo.save(brandEntity);
	}

	@Override
	public List<BrandResponseDto> getAllBrands() {
		List<BrandEntity> allBrands = brandRepo.findAll();
		return allBrands.stream().map(BrandMapper::brandEntityToBrandRespDto).collect(Collectors.toList());
	}

	@Override
	public String deleteBrandById(Integer id) {
		getOneBrand(id);
		brandRepo.deleteById(id);
		return "Brand with id: " + id + " is deleted Successfully";
	}

	@Override
	public BrandResponseDto getOneBrand(Integer id) {
		BrandEntity foundBrand = brandRepo.findById(id).orElseThrow(() -> new IdNotFoundException("Invalid id"));
		return BrandMapper.brandEntityToBrandRespDto(foundBrand);
	}

	@Override
	public String updateBrand(Integer id, BrandDto brandDto) {
		BrandEntity updatedBrand = BrandMapper.dtoToBrandEntity(brandDto);
		BrandEntity foundBrand = brandRepo.findById(id).orElseThrow(() -> new IdNotFoundException("Invalid id"));
		if (updatedBrand.getName() != null) {
			foundBrand.setName(updatedBrand.getName());
		}
		if (updatedBrand.getDescription() != null) {
			foundBrand.setDescription(updatedBrand.getDescription());
		}
		if (updatedBrand.getImageUrl() != null) {
			foundBrand.setImageUrl(updatedBrand.getImageUrl());
		}
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(brandDto.getUserId());
		foundBrand.setUpdatedBy(userEntity);
		brandRepo.save(foundBrand);
		return "Brand updated Successfully";
	}

}
