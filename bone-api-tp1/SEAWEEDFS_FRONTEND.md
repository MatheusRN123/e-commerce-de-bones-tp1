# Guia de Uso do SeaweedFS no Frontend

## 🔌 Endpoints Disponíveis

### Upload de Arquivo
```
POST /arquivos/upload
Content-Type: multipart/form-data

Response:
{
  "id": 1,
  "fid": "1,abc123xyz",
  "nomeOriginal": "imagem.png",
  "mimeType": "application/octet-stream",
  "tamanhoBytes": 1024,
  "sha256": "hash..."
}
```

### Download de Arquivo
```
GET /arquivos/download/{fid}
Response: Arquivo em bytes
```

---

## 🎯 Implementação no Frontend (Angular)

### 1. Criar o Serviço

```typescript
// arquivo.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ArquivoResponse {
  id: number;
  fid: string;
  nomeOriginal: string;
  mimeType: string;
  tamanhoBytes: number;
  sha256: string;
}

@Injectable({
  providedIn: 'root'
})
export class ArquivoService {
  private apiUrl = 'http://localhost:8080/arquivos';

  constructor(private http: HttpClient) {}

  upload(arquivo: File): Observable<ArquivoResponse> {
    const formData = new FormData();
    formData.append('arquivo', arquivo);
    
    return this.http.post<ArquivoResponse>(`${this.apiUrl}/upload`, formData);
  }

  download(fid: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/download/${fid}`, {
      responseType: 'blob'
    });
  }
}
```

### 2. Usar em um Componente

```typescript
// produto.component.ts
import { Component } from '@angular/core';
import { ArquivoService, ArquivoResponse } from './arquivo.service';

@Component({
  selector: 'app-produto',
  templateUrl: './produto.component.html'
})
export class ProdutoComponent {
  imagemUrl: string | null = null;
  arquivoFid: string | null = null;
  carregando = false;

  constructor(private arquivoService: ArquivoService) {}

  onFileSelected(event: any): void {
    const arquivo: File = event.target.files[0];
    if (arquivo) {
      this.carregando = true;
      this.arquivoService.upload(arquivo).subscribe({
        next: (resposta: ArquivoResponse) => {
          this.arquivoFid = resposta.fid;
          // Gerar URL de preview
          this.imagemUrl = this.gerarUrlPreview(resposta.fid);
          this.carregando = false;
          console.log('Upload realizado:', resposta);
        },
        error: (erro) => {
          console.error('Erro ao fazer upload:', erro);
          this.carregando = false;
        }
      });
    }
  }

  gerarUrlPreview(fid: string): string {
    return `${this.arquivoService['apiUrl']}/download/${fid}`;
  }

  downloadArquivo(): void {
    if (this.arquivoFid) {
      this.arquivoService.download(this.arquivoFid).subscribe(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'arquivo';
        a.click();
        window.URL.revokeObjectURL(url);
      });
    }
  }
}
```

### 3. Template HTML

```html
<!-- produto.component.html -->
<div class="upload-section">
  <label for="fileInput">Selecione uma imagem:</label>
  <input 
    id="fileInput" 
    type="file" 
    accept="image/*" 
    (change)="onFileSelected($event)" 
    [disabled]="carregando"
  />
  
  <div *ngIf="carregando" class="spinner">
    Carregando...
  </div>

  <div *ngIf="imagemUrl" class="preview-section">
    <h3>Preview da Imagem</h3>
    <img [src]="imagemUrl" alt="Preview" width="300" />
    <p>FID: {{ arquivoFid }}</p>
    <button (click)="downloadArquivo()">Download</button>
  </div>
</div>
```

---

## 🚀 Como Funciona Internamente

1. **Upload:**
   - Frontend envia arquivo para `/arquivos/upload`
   - Backend calcula hash SHA256 do arquivo
   - Backend envia para SeaweedFS e obtém FID
   - Backend salva metadados (FID, nome, tamanho) no PostgreSQL
   - Frontend recebe o FID e pode usar para displays/downloads

2. **Download:**
   - Frontend envia FID para `/arquivos/download/{fid}`
   - Backend busca arquivo no SeaweedFS usando FID
   - Backend retorna bytes do arquivo

---

## 📝 Exemplo de Integração com Modelo de Estampa

```typescript
// estampa.service.ts
export interface EstampaDTO {
  id: number;
  nome: string;
  descricao: string;
  imagemFid: string; // FID do arquivo no SeaweedFS
}

adicionarEstampaComImagem(estampa: Partial<EstampaDTO>, arquivo: File): Observable<EstampaDTO> {
  const formData = new FormData();
  formData.append('estampa', JSON.stringify(estampa));
  formData.append('arquivo', arquivo);
  
  return this.http.post<EstampaDTO>(`${this.apiUrl}/estampas`, formData);
}

obterEstampaComImagem(id: number): Observable<any> {
  return this.http.get<EstampaDTO>(`${this.apiUrl}/estampas/${id}`).pipe(
    map(estampa => ({
      ...estampa,
      imagemUrl: `/arquivos/download/${estampa.imagemFid}`
    }))
  );
}
```

---

## ⚙️ Configuração Necessária no Backend

Já está configurado em `application.properties`:

```properties
seaweedfs.master.url=http://localhost:9333
seaweedfs.request-timeout-ms=10000
%dev.seaweedfs.devservice.enabled=true
```

---

## 🐳 Verificar se SeaweedFS está Rodando

```bash
# Verificar container em desenvolvimento
docker ps | grep seaweedfs

# Acessar interface web
http://localhost:8111
```

---

## 📦 Dependências Necessárias

As dependências já devem estar no pom.xml:
- `quarkus-rest`
- `quarkus-rest-jackson`
- `quarkus-hibernate-orm-panache`

Se não estiverem, adicionar:
```xml
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-rest-multipart</artifactId>
</dependency>
```
