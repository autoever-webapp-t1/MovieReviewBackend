package com.movie.MovieReview.admin.service;

import com.movie.MovieReview.admin.dto.AwardAddMoviesDto;
import com.movie.MovieReview.awards.entity.AwardsEntity;
import com.movie.MovieReview.awards.repository.AwardsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {
    private final AwardsRepository awardsRepository;

    public AdminServiceImpl(AwardsRepository awardsRepository) {
        this.awardsRepository = awardsRepository;
    }

    @Transactional
    public AwardsEntity addAwardWithMovies(AwardAddMoviesDto dto) throws Exception {
        AwardsEntity award = new AwardsEntity();
        award.setAwardName(dto.getAwardName());
        Long[] movieIds = dto.getMovieIds();
        for (int i=0; i<movieIds.length; i++) {
            if (movieIds[i]!=null) {
                switch (i) {
                    case 0:
                        award.setNominated1(movieIds[i]);
                        break;
                    case 1:
                        award.setNominated2(movieIds[i]);
                        break;
                    case 2:
                        award.setNominated3(movieIds[i]);
                         break;
                    case 3:
                        award.setNominated4(movieIds[i]);
                        break;
                }
            }
        }
        return awardsRepository.save(award);
    }
}
