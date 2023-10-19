package dio.labpadroesprojetospring.service.implementacao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dio.labpadroesprojetospring.model.Cliente;
import dio.labpadroesprojetospring.model.ClienteRepository;
import dio.labpadroesprojetospring.model.Endereco;
import dio.labpadroesprojetospring.model.EnderecoRepository;
import dio.labpadroesprojetospring.service.ClienteService;
import dio.labpadroesprojetospring.service.ViaCepService;

@Service
public class ClienteServiceImplementacao implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
            
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
         Optional<Cliente> clienteBd = clienteRepository.findById(id);
         if(clienteBd.isPresent()){
            salvarClienteComCep(cliente);
         }
       
    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id); 
    }
    
      private void salvarClienteComCep(Cliente cliente) {
        
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
}
