import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CampaignDto } from '../../shared/dtos/campaign.dto';

@Injectable({
  providedIn: 'root'
})
export class CampaignService {

  public constructor(private httpClient: HttpClient) { }

  public getAll(): Observable<CampaignDto[]> {
    return this.httpClient.get<CampaignDto[]>(
      'http://localhost:8080/api/campaign'
    );
  }

  public getById(id: number): Observable<CampaignDto> {
    return this.httpClient.get<CampaignDto>(
      `http://localhost:8080/api/campaign/${id}`
    );
  }

  public create(campaignDto: CampaignDto): Observable<CampaignDto> {
    return this.httpClient.post<CampaignDto>(
      'http://localhost:8080/api/campaign',
      campaignDto
    );
  }

  public edit(id: number, campaignDto: CampaignDto): Observable<CampaignDto> {
    return this.httpClient.put<CampaignDto>(
      `http://localhost:8080/api/campaign/${id}`,
      campaignDto
    );
  }

  public delete(id: string): Observable<string> {
    return this.httpClient.delete(`http://localhost:8080/api/campaign/${id}`, {
      responseType: 'text',
    });
  }
}
