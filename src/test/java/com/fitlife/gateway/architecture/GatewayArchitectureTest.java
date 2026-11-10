
package com.fitlife.gateway.architecture;

import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.fitlife.gateway")
class GatewayArchitectureTest {

    @ArchTest
    static final ArchRule bff_layers_must_respect_dependencies = 
    layeredArchitecture()
            .consideringOnlyDependenciesInAnyPackage("com.fitlife.gateway..")

            .layer("Controller").definedBy("..bff.controller..")
            .layer("Service").definedBy("..bff.service..")
            .layer("Client").definedBy("..bff.client..")
            .layer("BffDto").definedBy("..bff.dto..")
            .layer("ExceptionHandler").definedBy("..exception")
            .layer("ExceptionDto").definedBy("..exception.dto..")

            .whereLayer("Controller")
            .mayOnlyAccessLayers("Service", "BffDto")

            .whereLayer("Service")
            .mayOnlyAccessLayers("Client", "BffDto")

            .whereLayer("Client")
            .mayOnlyBeAccessedByLayers("Service")

            .whereLayer("Service")
            .mayOnlyBeAccessedByLayers("Controller")

            .whereLayer("BffDto")
            .mayOnlyBeAccessedByLayers("Controller", "Service")

            .whereLayer("ExceptionHandler")
            .mayOnlyAccessLayers("ExceptionDto")

            .whereLayer("ExceptionDto")
            .mayOnlyBeAccessedByLayers("ExceptionHandler");

    @ArchTest
    static final ArchRule controllers_must_not_access_clients = noClasses()
            .that()
            .resideInAPackage("..bff.controller..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..bff.client..");

    @ArchTest
    static final ArchRule dtos_must_not_depend_on_application_layers = noClasses()
            .that()
            .resideInAnyPackage(
                    "..bff.dto..",
                    "..exception.dto..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                    "..bff.controller..",
                    "..bff.service..",
                    "..bff.client..",
                    "..exception");

    @ArchTest
    static final ArchRule gateway_packages_must_be_free_of_cycles = slices()
            .matching("com.fitlife.gateway.(*)..")
            .should()
            .beFreeOfCycles();
}