-- select 1;


truncate table cargo restart identity cascade;

truncate table categoria restart identity cascade;

truncate table cliente restart identity cascade;

truncate table endereco restart identity cascade;

truncate table estoque restart identity cascade;

truncate table farmacia restart identity cascade;

truncate table feedback restart identity cascade;

truncate table fornecedor restart identity cascade;

truncate table funcionario restart identity cascade;

truncate table item_pedido restart identity cascade;

truncate table notificacao restart identity cascade;

truncate table pagamento restart identity cascade;

truncate table pedido_compra restart identity cascade;

truncate table permissao restart identity cascade;

truncate table produto restart identity cascade;

truncate table receita restart identity cascade;

truncate table tarja restart identity cascade;

truncate table venda restart identity cascade;

-- Endereços

INSERT INTO endereco
(bairro, cep, cidade, complemento, logradouro, numero, uf)
VALUES('Fábrica', '36080-001','Juiz de Fora', ' ', 'R. Bernardo Mascarenhas', '1283', 'MG'),
      ('São Pedro', '36036-900', 'Juiz de Fora', 'Campus Universitário', 'R. José Lourenço Kelmer', 's/n', 'MG'),
      ('Fábrica', '36080-001', 'Juiz de Fora', ' ', 'R. Bernardo Mascarenhas', '1045', 'MG'),
      ('Fábrica', '36080-001', 'Juiz de Fora', ' ', 'R. Bernardo Mascarenhas', '223', 'MG'),
      ('Fábrica', '36080-001', 'Juiz de Fora', ' ', 'R. Bernardo Mascarenhas', '784', 'MG'),
      ('Fábrica', '36080-001', 'Juiz de Fora', ' ', 'R. Bernardo Mascarenhas', '1283', 'MG');

-- Farmácias

INSERT INTO public.farmacia
(cnpj, email, nome, telefone, endereco_id)
VALUES('3123123123', 'farmacianumero1@gmail.com', 'Farmácia 1', '4444-4444', 1),
      ('3123123123', 'farmacianumero2@gmail.com', 'Farmácia 2', '5555-5555', 2),
      ('3123123123', 'farmacianumero3@gmail.com', 'Farmácia 3', '6266-6266', 3);

-- Cargos

INSERT INTO public.cargo
(nome)
VALUES('Farmacêutico'),
      ('Gerente'),
      ('Administrador');

-- Permissões

INSERT INTO public.permissao
(nome, cargo_id)
VALUES('Cadastrar Produtos', 1),
      ('Cadastrar Produtos', 2),
      ('Cadastrar Funcionários', 2),
      ('Cadastrar Funcionários', 3),
      ('Cadastrar Farmácias', 3);

-- Categorias

INSERT INTO public.categoria
(descricao, nome, categoria_pai_id)
VALUES('Medicamento', 'Medicamentos no geral.', null),
      ('Produto Alimentício', 'Produtos alimentícios em geral.', null),
      ('Betabloqueadores', 'Calmantes.', 1),
      ('Ração Animal', 'Ração e alimentos para animais.', 2),
      ('Suplemento alimentício', 'Medicamentos destinados a complementar a nutrição.', 2),
      ('Benzodiazepínicos', 'Não sei o que isso faz mas é remédio', 1);

-- Tarjas

INSERT INTO public.tarja
(cor, nome, requer_receita, retem_receita)
VALUES('Vermelha', 'Vermelha sem Retenção', true, false),
      ('Vermelha', 'Vermelha com Retenção', true, true),
      ('Preta', 'Preta', true, true),
      ('Sem Tarja', '', false, false);

-- Produtos

INSERT INTO public.produto
(descricao, generico, nome, peso, preco, requer_lote, categoria_id, tarja_id)
VALUES('Faz bem para a saúde.', false, 'Omega 3', 50, 50.0, true, 5, 4),
      ('Sachê de ração para gatos.', false, 'Sachê Whiskas', 100, 4.0, true, 4, 4),
      ('Cloridrato de Propranolol 40 mg, genérico Pharlab, 30 comprimidos.', true, 'Cloridrato de Propranolol', 50, 8.99, true, 3, 2),
      ('Faz bem para convulsão, pânico e ansiedade.', false, 'Clonazepam', 60, 80.0, true, 6, 3);

-- Fornecedores

INSERT INTO public.fornecedor
(cnpj, email, nome, telefone, endereco_id)
VALUES
    ('3123123123', 'fornecedorletraa@email.com', 'Fornecedor A', '4444-4444', 1),
    ('3123123123', 'fornecedorletrab@email.com', 'Fornecedor B', '4444-4444', 3),
    ('3123123123', 'fornecedorletrac@email.com', 'Fornecedor C', '4444-4444', 2);

-- Estoques
-- coluna type representa qual tipo de estoque é, sendo valores válidos "estoque" ou "estoqueLote".

INSERT INTO public.estoque
("type", quantidade, data_fabricacao, data_validade, lote, farmacia_id, fornecedor_id, produto_id)
VALUES('estoqueLote', 10, '10-10-2024', '30-02-2025', '10-2024-0038', 1, 1, 1),
      ('estoqueLote', 50, '07-10-2024', '15-08-2025', '10-2024-0012', 1, 1, 2),
      ('estoqueLote', 34, '02-10-2024', '27-09-2025', '10-2024-0002', 1, 1, 3),
      ('estoqueLote', 21, '02-11-2024', '27-10-2025', '11-2024-0107', 1, 1, 3);

-- Clientes

WITH novo_usuario AS (
INSERT INTO public.usuario (cpf, data_admissao, email, nome, senha, telefone, endereco_id)
VALUES ('11111111111', '2014-11-11', 'usuario01@gmail.com', 'usuario01 da silva', '1dasilva', '1111-1111', 1)
    RETURNING id
    )
INSERT INTO public.cliente (usuario_id,  fidelidade_pontos)
SELECT id, 0.0
FROM novo_usuario;
--
WITH novo_usuario AS (
INSERT INTO public.usuario (cpf, data_admissao, email, nome, senha, telefone, endereco_id)
VALUES ('22222222222', '11-11-2014', 'usuario02@gmail.com', 'usuario02 de carvalho', '2decarvalho', '2222-2222', 2)
    RETURNING id
    )
INSERT INTO public.cliente (usuario_id,  fidelidade_pontos)
SELECT id, 0.0
FROM novo_usuario;
--
WITH novo_usuario AS (
INSERT INTO public.usuario (cpf, data_admissao, email, nome, senha, telefone, endereco_id)
VALUES ('33333333333', '12-11-2014', 'usuario03@gmail.com', 'usuario03 pereira', '3pereira', '3333-3333', 3)
    RETURNING id
    )
INSERT INTO public.cliente (usuario_id,  fidelidade_pontos)
SELECT id, 0.0
FROM novo_usuario;

-- Funcionários

WITH novo_usuario AS (
INSERT INTO public.usuario (cpf, data_admissao, email, nome, senha, telefone, endereco_id)
VALUES ('44444444444', '11-11-2014', 'usuario04@gmail.com', 'usuario04 ferreira', '4ferreira', '4444-4444', 4)
    RETURNING id
    )
INSERT INTO public.funcionario (usuario_id,  expediente, salario, cargo_id, farmacia_id)
SELECT id, 'manha', 1050.0, 1, 1
FROM novo_usuario;
--
WITH novo_usuario AS (
INSERT INTO public.usuario (cpf, data_admissao, email, nome, senha, telefone, endereco_id)
VALUES ('55555555555', '11-11-2014', 'usuario05@gmail.com', 'usuario05 oliveira', '5oliveira', '5555-5555', 5)
    RETURNING id
    )
INSERT INTO public.funcionario (usuario_id,  expediente, salario, cargo_id, farmacia_id)
SELECT id, 'tarde', 2010.0, 2, 2
FROM novo_usuario;
--
WITH novo_usuario AS (
INSERT INTO public.usuario (cpf, data_admissao, email, nome, senha, telefone, endereco_id)
VALUES ('66666666666', '11-11-2014', 'usuario06@gmail.com', 'usuario06 lopes', '6lopes', '6666-6666', 6)
    RETURNING id
    )
INSERT INTO public.funcionario (usuario_id,  expediente, salario, cargo_id, farmacia_id)
SELECT id, 'manha', 1450.0, 3, 3
FROM novo_usuario;

-- Pedidos de Compra

INSERT INTO public.pedido_compra
(codigo, data_criacao, data_entrega, status, status_entrega, tipo_entrega, valor_total, endereco_id)
VALUES
    ('20241113000000', '13-11-2024', '19-11-2024', 'finalizado', 'entregue', 'delivery', 150, 2),
    ('20241113000001', '13-11-2024', '', 'entrega pendente', 'pendente', 'busca no estabelecimento', 71.98, null),
    ('20241113000002', '13-11-2024', '', 'pagamento pendente', 'pendente', 'delivery', 17.98, 1);

-- Itens dos Pedidos de Compra
-- TODO: Esse dos itens pedido e o resto das outras classes. Pra criar esses inserts, eu estou usando o DBeaver
--  (clica com botão direito na tabela com o banco de dados aberto e clica em gerar sql, ai vai em insert e copia o
--  código, e depois de colar muda o ID pra um valor fixo pra teste, pra ficar previsível e não dar problema).

-- INSERT INTO public.item_pedido
-- (preco_unitario, quantidade, estoque_id, pedido_compra_id, receita_id)
-- VALUES(0, 0, 0, 0, 0);

