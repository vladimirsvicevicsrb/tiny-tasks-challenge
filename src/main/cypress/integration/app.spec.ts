describe('Basic tests', () => { 

  beforeEach(() => {
    cy.visit ('http://localhost:4200');
  });

  it ('Check application title', () => {
     cy.title().should('eq', 'Resourcify | TinyTasks');
  });

  it('Check task form fields', () => {
    cy.get('form').within(() => {
      cy.get('input[formControlName="name"]').should('be.visible');
      cy.get('input[formControlName="date"]').should('be.visible');
      cy.get('input[formControlName="time"]').should('be.visible');

      cy.get('label[for="mat-input-0"] span').should('have.text', 'Enter the task name *');
      cy.get('label[for="mat-input-1"] span').should('have.text', 'Select date');
      cy.get('label[for="mat-input-2"] mat-label').should('have.text', 'Select time');

      cy.get('input[type="file"]').should('be.visible');
      cy.get('button[type="submit"] span.mat-button-wrapper').should('have.text', 'add Add task ');
    });
  });

});