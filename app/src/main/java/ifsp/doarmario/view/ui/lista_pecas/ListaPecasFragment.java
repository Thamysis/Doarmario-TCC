package ifsp.doarmario.view.ui.lista_pecas;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ifsp.doarmario.R;
import ifsp.doarmario.model.dao.RecyclerItemClickListener;
import ifsp.doarmario.model.dao.VestuarioDAO;
import ifsp.doarmario.model.vo.Vestuario;
import ifsp.doarmario.view.adapter.VestuarioAdapter;
import ifsp.doarmario.view.ui.detalhamento_pecas.DetalhamentoPecasFragment;

public class ListaPecasFragment extends Fragment {
    private RecyclerView recyclerView;
    private VestuarioAdapter vestuarioAdapter;
    private List<Vestuario> listaVestuario = new ArrayList<>();
    private Vestuario vestuarioSelecionado;

    private String nomeUsuarioAtual;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listagem_pecas, container, false);

        //recuperar nome do usuário atual
        nomeUsuarioAtual = (String) getActivity().getIntent().getSerializableExtra("usuario");

        //Configurar recycler
        recyclerView = view.findViewById(R.id.recyclerView);

        carregarListaVestuario();

        //Adicionar evento de clique
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity().getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Recuperar vestuario para edicao
                                vestuarioSelecionado = listaVestuario.get( position );

                                DetalhamentoPecasFragment detalhamentoPecasFragment = new DetalhamentoPecasFragment();

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("vestuarioSelecionado", vestuarioSelecionado);
                                detalhamentoPecasFragment.setArguments(bundle);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                fragmentTransaction.replace(R.id.nav_host_fragment, detalhamentoPecasFragment);
                                fragmentManager.popBackStack();
                                fragmentTransaction.commit();

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                //Recupera tarefa para deletar
                                vestuarioSelecionado = listaVestuario.get( position );

                                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

                                //Configura título e mensagem
                                dialog.setTitle("Confirmar exclusão");
                                dialog.setMessage("Deseja excluir o vestuario: " + vestuarioSelecionado.getDescricao_vestuario() + " ?" );

                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        VestuarioDAO vestuarioDAO = new VestuarioDAO();
                                        if ( vestuarioDAO.deletar(vestuarioSelecionado) ){

                                            carregarListaVestuario();
                                            Toast.makeText(getActivity().getApplicationContext(),
                                                    "Sucesso ao excluir vestuário!",
                                                    Toast.LENGTH_SHORT).show();

                                        }else {
                                            Toast.makeText(getActivity().getApplicationContext(),
                                                    "Erro ao excluir vestuário!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                dialog.setNegativeButton("Não", null );

                                //Exibir dialog
                                dialog.create();
                                dialog.show();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        return view;
    }

    public void carregarListaVestuario(){
        //Listar vestuario
        VestuarioDAO vestuarioDAO = new VestuarioDAO();
        listaVestuario = vestuarioDAO.listar(nomeUsuarioAtual);
        //Configurar um adapter
        vestuarioAdapter = new VestuarioAdapter(listaVestuario );

        //Configurar Recyclerview
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3 );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(vestuarioAdapter);

    }


}