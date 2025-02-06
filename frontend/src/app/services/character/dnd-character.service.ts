import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DndCharacterDto } from '../../shared/dtos/dnd-character.dto';

@Injectable({
  providedIn: 'root'
})
export class DndCharacterService {

  public constructor(private httpClient: HttpClient) { }
  
  public getAll(): Observable<DndCharacterDto[]> {
    return this.httpClient.get<DndCharacterDto[]>(
      'http://localhost:8080/api/character'
    );
  }
  
  public getById(id: number): Observable<DndCharacterDto> {
    return this.httpClient.get<DndCharacterDto>(
      `http://localhost:8080/api/character/${id}`
    );
  }
  
  public create(dndCharacterDto: DndCharacterDto): Observable<DndCharacterDto> {
    return this.httpClient.post<DndCharacterDto>(
      'http://localhost:8080/api/character',
      dndCharacterDto
    );
  }
  
  public edit(id: number, dndCharacterDto: DndCharacterDto): Observable<DndCharacterDto> {
    return this.httpClient.put<DndCharacterDto>(
      `http://localhost:8080/api/character/${id}`,
      dndCharacterDto
    );
  }
  
  public delete(id: string): Observable<string> {
    return this.httpClient.delete(`http://localhost:8080/api/character/${id}`, {
      responseType: 'text',
    });
  }
}
