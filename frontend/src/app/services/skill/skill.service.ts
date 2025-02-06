import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SkillDto } from '../../shared/dtos/skill.dto';

@Injectable({
  providedIn: 'root'
})
export class SkillService {

  public constructor(private httpClient: HttpClient) { }
    
  public getAll(): Observable<SkillDto[]> {
    return this.httpClient.get<SkillDto[]>(
      'http://localhost:8080/api/skill'
    );
  }

  public getById(id: number): Observable<SkillDto> {
    return this.httpClient.get<SkillDto>(
      `http://localhost:8080/api/skill/${id}`
    );
  }
}
