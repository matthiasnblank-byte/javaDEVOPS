describe('Todos E2E', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/todos', [
      { id: 1, title: 'Eins', completed: false },
      { id: 2, title: 'Zwei', completed: true },
    ]).as('list');

    cy.visit('/');
    cy.wait('@list');
  });

  it('zeigt Liste und kann Todo hinzufügen', () => {
    cy.get('[data-cy="todo-list"] li').should('have.length', 2);

    cy.intercept('POST', '/api/todos', { id: 3, title: 'Neu', completed: false }).as('create');
    cy.get('[data-cy="new-todo-input"]').type('Neu');
    cy.get('[data-cy="add-btn"]').click();
    cy.wait('@create');
  });
});

